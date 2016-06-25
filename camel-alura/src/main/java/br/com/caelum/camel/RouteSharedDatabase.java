package br.com.caelum.camel;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import com.thoughtworks.xstream.XStream;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.xstream.XStreamDataFormat;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by wallace on 25/03/16.
 */
public class RouteSharedDatabase {

    private static MysqlConnectionPoolDataSource dataSource() {
        MysqlConnectionPoolDataSource mysqlDS = new MysqlConnectionPoolDataSource();
        mysqlDS.setDatabaseName("camel");
        mysqlDS.setServerName("localhost");
        mysqlDS.setPort(3306);
        mysqlDS.setUser("root");
        mysqlDS.setPassword("123456");
        return mysqlDS;
    }

    static class NegociacaoProcessor implements Processor {

        @Override
        public void process(Exchange exchange) throws Exception {
            Negociacao negociacao = exchange.getIn().getBody(Negociacao.class);
            exchange.setProperty("preco", negociacao.getPreco());
            exchange.setProperty("quantidade", negociacao.getQuantidade());
            String data = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(negociacao.getData().getTime());
            exchange.setProperty("data", data);
        }

    }

    public static void main(String[] args) throws Exception {

        SimpleRegistry registry = new SimpleRegistry();
        registry.put("mysql", dataSource());

        ExecutorService executorService = Executors.newWorkStealingPool(20);

        CamelContext context = new DefaultCamelContext(registry);

        final XStream xstream = new XStream();
        xstream.alias("negociacao", Negociacao.class);

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("timer://negociacoes?fixedRate=true&period=180000&repeatCount=10")
                        .to("http4://argentumws.caelum.com.br/negociacoes")
                        .convertBodyTo(String.class)
                        .unmarshal(new XStreamDataFormat(xstream))
                        .split(body()) //cada negociação se torna uma mensagem
                        .parallelProcessing().executorService(executorService) // Processamento Paralelo
                        .process(new NegociacaoProcessor())
                        //.log("${body}")
                        .setBody(simple("insert into negociacao(preco, quantidade, data) values (${property.preco}, ${property.quantidade}, '${property.data}')"))
                        .log("${threadName} ${body}")
                        .delay(1000) //usado apenas pare debug, esperando 1s para deixar a execução mais fácil de entender
                        .to("jdbc:mysql"); //usando o componente jdbc que envia o SQL para mysql
            }
        });

        context.start();
        TimeUnit.MINUTES.sleep(20);
        context.stop();
    }

}
