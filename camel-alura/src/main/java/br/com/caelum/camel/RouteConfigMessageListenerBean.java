package br.com.caelum.camel;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.concurrent.TimeUnit;

/**
 * Created by wallace on 20/04/16.
 */
public class RouteConfigMessageListenerBean {


    public static void main(String[] args) throws Exception {

        CamelContext context = new DefaultCamelContext();
        context.addComponent("jms", ActiveMQComponent.activeMQComponent("tcp://localhost:61616"));

        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {

                from("jms:queue:pedidos")
                    .bean(GatewayJMS.class)
                    .log("Pattern: ${exchange.pattern}")
                .to("mock:jms");

            }
        });


        context.start();
        TimeUnit.MINUTES.sleep(5);
        context.stop();

    }

}
