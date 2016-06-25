package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.concurrent.TimeUnit;

/**
 * Created by wallace on 17/04/16.
 */
public class RouteVelocityTemplate {


    public static void main(String[] args) throws Exception {

        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:entrada")
                    .setHeader("data", constant("Domingo, 17 de abril de 2016"))
                    .setHeader("valor", constant("R$ 1.500,00"))
                    .setHeader("nsu", constant("LPOP332300989812KJJFD"))
                    .to("velocity:template.vm")
                    .log("${body}");
            }
        });

        context.start();

        ProducerTemplate template = context.createProducerTemplate();
        template.sendBody("direct:entrada", "Apache Camel rocks!!!");

        TimeUnit.SECONDS.sleep(5);
        context.stop();

    }

}
