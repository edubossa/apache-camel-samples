package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.concurrent.TimeUnit;

/**
 * Created by wallace on 17/04/16.
 */
public class RouteXSTLtoHTML {

    public static void main(String[] args) throws Exception {

        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("file:movimentacao?delay=5s&noop=true")
                    .to("xslt:movimentacao-para-html.xslt")
                    .log("${body}")
                    .setHeader("CamelFileName", simple("${file:onlyname.noext}.html"))
                .to("file:movimentacao");
            }
        });

        context.start();
        TimeUnit.SECONDS.sleep(5);
        context.stop();

    }

}
