package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.concurrent.TimeUnit;

/**
 * Created by wallace on 26/03/16.
 */
public class RouteDirectProducerTemplate {

    public static void main(String[] args) throws Exception {

        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {

                from("direct:soap")
                    .routeId("route-soap")
                    .log("chamando servicos soap ${body}")
                .to("mock:soap");

            }
        });

        context.start();

        for (int i = 0; i < 10; i++) {
            //Envia uma mensagem programaticamente para um rota que começa com direct
            ProducerTemplate template = context.createProducerTemplate();
            template.sendBody("direct:soap", "<pedido>Enviando pedido Producer Template test</pedido>");
            //producer.asyncSendBody("direct:soap", "<pedido>Enviando pedido Producer Template test</pedido>");
            TimeUnit.SECONDS.sleep(1);
        }

        context.stop();

    }

}
