package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.concurrent.TimeUnit;

/**
 * Created by wallace on 22/03/16.
 */
public class RouteFilterSpliter {

    public static void main(String[] args) throws Exception {

        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("file:pedidos?delay=5s&noop=true"). //busca os arquivos na pasta pedidos
                    split().xpath("/pedido/itens/item"). //quebro o processamento por item
                    filter().xpath("/item/formato[text()='EBOOK']").log("Log Filter \n ${body}"). //filtra o item pelo formato
                    marshal().xmljson().log("${body}"). //faz parser para json
                    setHeader("CamelFileName", simple("${file:onlyname.noext}.json")). //seta o mesmo nome do arquivo .json
                to("file:/Users/wallace/Projetos/saida"); //envia para pasta saida
            }
        });

        context.start();
        TimeUnit.SECONDS.sleep(20);
        context.stop();
        System.out.println("Process Camel Finalizado!");

    }

}
