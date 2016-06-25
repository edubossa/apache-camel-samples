/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.argonavis.cursocamel.router;

import br.com.argonavis.cursocamel.Configuration;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

/**
 * OK!
 *
 * @author helderdarocha
 */
public class FilterExample {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        ConnectionFactory cf = new ActiveMQConnectionFactory(Configuration.ACTIVEMQ_URL);
        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(cf));

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                // Rota 1 – Do sistema de arquivos para a fila inbound-topic
                from("file:lab/inbox?noop=true")
                    .setHeader("Name", header("CamelFileNameOnly"))
                    .setHeader("Length", header("CamelFileLength"))
                    .process(new Processor() {
                        public void process(Exchange exchange) throws Exception {
                            String name = exchange.getIn().getHeader("Name", String.class);
                            String type = name.substring(name.lastIndexOf('.') + 1).toLowerCase();
                            exchange.getIn().setHeader("Type", type);
                        }
                    })
                    .to("jms:topic:inbound-topic");

                // Rotas de filtros
                from("jms:topic:inbound-topic")
                    .process(new Processor() {
                        public void process(Exchange exchange) throws Exception {
                            System.out.println("PNG filter received: "
                                    + exchange.getIn().getHeader("Name"));
                        }
                    })
                    .filter(header("Type").isEqualTo("png"))
                        .process(new Processor() {
                            public void process(Exchange exchange) throws Exception {
                                System.out.println("PNG filter SELECTED: "
                                        + exchange.getIn().getHeader("Name"));
                            }
                        })
                    .to("jms:queue:dt-queue-1"); //OBS: so e enviado para fila caso passe no filtro

                from("jms:topic:inbound-topic")
                    .process(new Processor() {
                        public void process(Exchange exchange) throws Exception {
                            System.out.println("TXT filter received: "
                                    + exchange.getIn().getHeader("Name"));
                        }
                    })
                    .filter(header("Type").isEqualTo("txt"))
                    .process(new Processor() {
                        public void process(Exchange exchange) throws Exception {
                            System.out.println("TXT filter SELECTED: "
                                    + exchange.getIn().getHeader("Name"));
                        }
                    })
                    .to("jms:queue:dt-queue-2"); //OBS: so e enviado para fila caso passe no filtro

            }
        });

        context.start();

        System.out.println("O servidor está no ar por 60 segundos.");
        Thread.sleep(60000);
        context.stop();
    }
}
