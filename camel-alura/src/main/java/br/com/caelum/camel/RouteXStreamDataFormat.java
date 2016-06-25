package br.com.caelum.camel;

import com.thoughtworks.xstream.XStream;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.xstream.XStreamDataFormat;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.concurrent.TimeUnit;

/**
 * Created by wallace on 25/03/16.
 */
public class RouteXStreamDataFormat {


    public static void main(String[] args) throws Exception {

        CamelContext context = new DefaultCamelContext();

        final XStream xstream = new XStream();
        xstream.alias("negociacao", Negociacao.class);

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                from("timer://negociacoes?fixedRate=true&period=5000&repeatCount=1")
                        .to("http4://argentumws.caelum.com.br/negociacoes")
                        .convertBodyTo(String.class)
                        .unmarshal(new XStreamDataFormat(xstream))
                        .split(body()).log("${body}") //cada negociação se torna uma mensagem
                        .end(); //sinaliza apenas o fim da rota
//                        .setHeader(Exchange.FILE_NAME, constant("negociacoes.xml"))
//                        .to("file:/Users/wallace/Projetos/saida");
            }
        });



        context.start();
        TimeUnit.MINUTES.sleep(3);
        context.stop();

    }

}
