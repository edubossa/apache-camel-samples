package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.concurrent.TimeUnit;

/**
 * Created by wallace on 28/03/16.
 * <br/><br/>
 * Há uma alternativa ao direct e multicast. Na rota e sub-rotas podemos aplicar algo chamado de Staged event-driven
 * architecture ou simplesmente SEDA.
 * <br/>
 * A ideia do SEDA é que cada rota (e sub-rota) possua a sua fila dedicada de entrada e as rotas enviam mensagens para
 * essas filas para se comunicar. Dentro dessa arquitetura as mensagens são chamadas de eventos.
 * <br/>
 * A rota fica então consumindo as mensagens/eventos dessa fila, tudo em paralelo.
 * <br/>
 * Para usar SEDA basta substituir a palavra direct por seda. O multicast não é mais necessário.
 *
 */
public class RouteSEDA {


    public static void main(String[] args) throws Exception {

        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {

                from("file:pedidos?delay=5s&noop=true")
                        .routeId("route-pedidos")
                        .to("seda:http")
                        .to("seda:soap");

                from("seda:http").
                        routeId("rota-http").
                        setProperty("pedidoId", xpath("/pedido/id/text()")).
                        setProperty("email", xpath("/pedido/pagamento/email-titular/text()")).
                        split().
                        xpath("/pedido/itens/item").
                        filter().
                        xpath("/item/formato[text()='EBOOK']").
                        log("${routeId} ${body}").
                        setProperty("ebookId", xpath("/item/livro/codigo/text()")).
                        setHeader(Exchange.HTTP_QUERY,
                                simple("clienteId=${property.email}&pedidoId=${property.pedidoId}&ebookId=${property.ebookId}")).
                        to("http4://localhost:8080/webservices/ebook/item");

                from("seda:soap")
                        .routeId("route-soap")
                        .log("${routeId} ${body}")
                        .to("mock:soap");
            }
        });

        context.start();
        TimeUnit.SECONDS.sleep(60);
        context.stop();
    }

}
