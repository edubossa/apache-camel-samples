package br.com.caelum.camel;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.concurrent.TimeUnit;

/**
 * Created by wallace on 20/04/16.
 */
public class RouteJMSActiveMQProducer {

    public static void main(String[] args) throws Exception {

        CamelContext context = new DefaultCamelContext();
        context.addComponent("jms", ActiveMQComponent.activeMQComponent("tcp://localhost:61616"));

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                //le os arquivos da pasta pedidos
                from("file:pedidos?noop=true")
                        .log("${threadName} - ${body}")
                        //envia para a fila de pedidos
                        .to("jms:queue:pedidos");
            }
        });

        context.start();
        TimeUnit.SECONDS.sleep(5);
        context.stop();

    }

}
