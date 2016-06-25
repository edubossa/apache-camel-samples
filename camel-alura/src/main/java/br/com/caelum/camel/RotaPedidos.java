package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.concurrent.TimeUnit;

public class RotaPedidos {

	public static void main(String[] args) throws Exception {

		CamelContext context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("file:pedidos?delay=5s&noop=true").
                    log("Processo Camel - ${id}").
                    marshal().xmljson().log("${body}").
                    setHeader("CamelFileName", simple("${file:onlyname.noext}.json")).
                to("file:/Users/wallace/Projetos/saida");
            }
        });

        System.out.println(context.getName() + " Iniciado!");
        context.start();
        TimeUnit.SECONDS.sleep(5);
        context.stop();
        System.out.println(context.getName() + " Finalizado!");


	}	
}
