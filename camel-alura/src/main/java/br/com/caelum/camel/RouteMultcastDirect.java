package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.concurrent.TimeUnit;

/**
 * Created by wallace on 26/03/16.
 */
public class RouteMultcastDirect {

    public static void main(String[] args) throws Exception {

        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {

                from("file:pedidos?delay=5s&noop=true")
                    .routeId("route-pedidos") //nomeia a rota  p/ obter um melhor debug

                    /*
                    * direciona a leitura de pedidos para ambas as rotas soap e http, caso nao seja adicionado o multicast
                    * as rotas seriam sequenciais, uma a uma processando o retorno de cada rota.
                    */
                    .multicast()
                        .parallelProcessing() //abre uma thread para o processamento de cada sub-rota
                        //.timeout(500)
                    .to("direct:soap") //subrota soap
                    .to("direct:http"); //subrota http

                from("direct:http")
                    .routeId("route-http")
                    .log("${routeId} -  ${threadName} - ${body}")
                    .setBody(constant("Route HTTP Component")) //constante usada para testes
                .to("mock:http"); //usado para testes

                from("direct:soap")
                    .routeId("route-soap")
                    .log("${routeId} -  ${threadName} - ${body}")
                    .setBody(constant("Route SOAP Component"))
                .to("mock:soap");
            }
        });

        context.start();
        TimeUnit.SECONDS.sleep(60);
        context.stop();

    }

}
