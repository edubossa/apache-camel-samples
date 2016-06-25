package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpMethods;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.concurrent.TimeUnit;

/**
 * Created by wallace on 26/03/16.
 */
public class RouteMultcastDirectPedidos {

    public static void main(String[] args) throws Exception {

        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {

                from("file:pedidos?delay=5s&noop=true")
                    .routeId("route-pedidos") //nomeia a rota  p/ obter um melhor debug
                    .multicast() //direciona os arquivos do pedidos para ambas as rotas
                    .to("direct:soap") //subrota soap
                    .to("direct:http"); //subrota http

                from("direct:http")
                        .routeId("route-http")
                        .setProperty("pedidoId" , xpath("/pedido/id/text()"))
                        .setProperty("clienteId", xpath("/pedido/pagamento/email-titular/text()"))
                        .split().xpath("/pedido/itens/item") //quebra o processamento por item
                        .filter().xpath("/item/formato[text()='EBOOK']")//filtra o item pelo formato
                        .setProperty("ebookId",  xpath("/item/livro/codigo/text()"))
                        .marshal().xmljson() //faz parser para json
                        .log("${routeId} -  ${body}")
                        .setHeader(Exchange.HTTP_METHOD, HttpMethods.GET)
                        .setHeader(Exchange.HTTP_QUERY, simple("ebookId=${property.ebookId}&pedidoId=${property.pedidoId}&clienteId=${property.clienteId}"))
                        .to("http4://localhost:8080/webservices/ebook/item");

                from("direct:soap")
                    .routeId("route-soap")
                    .log("${routeId} - ${body}")
                    .setBody(constant("<envelope>Route SOAP TESTE</envelope>"))
                .to("mock:soap");
            }
        });


        context.start();
        TimeUnit.SECONDS.sleep(5);
        context.stop();

    }

}
