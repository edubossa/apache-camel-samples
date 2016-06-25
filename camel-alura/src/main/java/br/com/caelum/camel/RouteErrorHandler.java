package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.concurrent.TimeUnit;

/**
 * Created by wallace on 18/04/16.
 */
public class RouteErrorHandler {


    public static void main(String[] args) throws Exception {

        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {

                //em caso de erro copia essa msg com erro para a pasta erro
                errorHandler(deadLetterChannel("file:erro")
                    //mantem o log no console
                    .logExhaustedMessageHistory(true)
                    //configura um numero maximo de retentativa
                    .maximumRedeliveries(3)
                    //configura o tempo de espera para a retentativa
                    .redeliveryDelay(2000)
                    //Obtem o evento de cada retentativa
                    .onRedelivery(new Processor() {
                        @Override
                        public void process(Exchange exchange) throws Exception {
                            int counter = (int) exchange.getIn().getHeader(Exchange.REDELIVERY_COUNTER);
                            int max = (int) exchange.getIn().getHeader(Exchange.REDELIVERY_MAX_COUNTER);
                            System.out.println("Redelivery " + counter + "/" + max);
                        }
                    })
                );

                from("file:pedidos?delay=5s&noop=true")
                        .routeId("route-pedidos") //nomeia a rota  p/ obter um melhor debug
                        .to("validator:pedido.xsd");
//                        .multicast() //direciona os arquivos do pedidos para ambas as rotas
//                        .to("direct:soap") //subrota soap
//                        .to("direct:http"); //subrota http
//
//                from("direct:http")
//                        .routeId("route-http")
//                        .setProperty("pedidoId" , xpath("/pedido/id/text()"))
//                        .setProperty("clienteId", xpath("/pedido/pagamento/email-titular/text()"))
//                        .split().xpath("/pedido/itens/item") //quebro o processamento por item
//                        .filter().xpath("/item/formato[text()='EBOOK']")//filtra o item pelo formato
//                        .setProperty("ebookId",  xpath("/item/livro/codigo/text()"))
//                        .marshal().xmljson() //faz parser para json
//                        //.log("${routeId} -  ${body}")
//                        .setHeader(Exchange.HTTP_METHOD, HttpMethods.GET)
//                        .setHeader(Exchange.HTTP_QUERY, simple("ebookId=${property.ebookId}&pedidoId=${property.pedidoId}&clienteId=${property.clienteId}"))
//                        .to("http4://localhost:8080/webservices/ebook/item");
//
//                from("direct:soap")
//                        .routeId("route-soap")
//                        .to("xslt:pedido-para-soap.xslt")
//                        .log("${routeId} - ${body}")
//                        .setHeader(Exchange.CONTENT_TYPE, constant("text/xml"))
//                        .to("http4://localhost:8080/webservices/financeiro");
            }

        });

        context.start();
        TimeUnit.SECONDS.sleep(5);
        context.stop();

    }

}
