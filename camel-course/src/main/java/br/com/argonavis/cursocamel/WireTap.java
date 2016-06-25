/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.argonavis.cursocamel;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

/**
 * OK!
 *
 * @author helderdarocha
 */
public class WireTap {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        
        // Connexao com o ActiveMQ
        ConnectionFactory cf = new ActiveMQConnectionFactory(Configuration.ACTIVEMQ_URL);
        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(cf));
        
        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {

                from("file:lab/outbox?noop=true").to("jms:objetos");

                from("jms:objetos")
                .wireTap("file:lab/wiretap")
                .choice()
                    .when(header("CamelFileName").regex("^.*(jpg|png|gif|jpeg)$"))
                        .to("jms:imagens")  
                    .when(header("CamelFileName").endsWith(".xml"))
                        .to("jms:docsXML")
                    .otherwise()
                        .to("jms:invalidos")
                        .stop() // estes objetos não seguem para próxima etapa
                .end() // fim do choice
                .to("jms:nextStep"); // proxima etapa (contem todos que não chamam stop())
                
                from("jms:imagens").process((Exchange exchange) -> {
                    System.out.println("Imagem recebida: " + exchange.getIn().getHeader("CamelFileName"));
                });                
                from("jms:docsXML").process((Exchange exchange) -> {
                    System.out.println("XML recebido: " + exchange.getIn().getHeader("CamelFileName"));
                    System.out.println(exchange.getIn().getBody());
                });
                from("jms:invalidos").process((Exchange exchange) -> {
                    System.out.println("Arquivo inválido: " + exchange.getIn().getHeader("CamelFileName"));
                });
                from("jms:nextStep").process((Exchange exchange) -> {
                    System.out.println("Arquivo copiado para próxima etapa: " + exchange.getIn().getHeader("CamelFileName"));
                });
            }
            
        });
        context.start();

        System.out.println("O servidor está no ar. Ponha arquivos em "
                + Configuration.OUTBOX
                + " para iniciar o processo."
                + "O servidor ficará no ar por 20 segundos.");
        Thread.sleep(20000);
        context.stop();
    }
}
