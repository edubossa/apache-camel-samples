package br.com.caelum.camel;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.concurrent.TimeUnit;

/**
 * Created by wallace on 19/04/16.
 */
public class RouteJMSActiveMQConsumer {


    public static void main(String[] args) throws Exception {

        CamelContext context = new DefaultCamelContext() ;
        context.addComponent("jms", ActiveMQComponent.activeMQComponent("tcp://localhost:61616"));

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                //consome as mensagens da fila de pedidos
                from("jms:queue:pedidos?concurrentConsumers=10&maxConcurrentConsumers=50")
                    .delay(500) //delay usado apenas para acompanhamento do debug
                    .log("${threadName} - ${body}")
                    .to("mock:jms");
            }
        });

        context.start();
        TimeUnit.MINUTES.sleep(60);
        context.stop();

    }

}
