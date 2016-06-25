package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.concurrent.TimeUnit;

/**
 * Created by wallace on 25/03/16.
 */
public class RouteTimer {

    public static void main(String[] args) throws Exception {

        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                from("timer://negociacoes?fixedRate=true&period=5000").log("${id}")
                    .to("http4://argentumws.caelum.com.br/negociacoes")
                        .setHeader(Exchange.FILE_NAME, constant("negociacoes.xml"))
                    .to("file:/Users/wallace/Projetos/saida");
            }
        });

        context.start();
        TimeUnit.MINUTES.sleep(3);
        context.stop();
    }
}
