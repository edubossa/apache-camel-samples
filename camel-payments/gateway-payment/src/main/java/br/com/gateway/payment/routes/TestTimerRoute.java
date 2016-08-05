package br.com.gateway.payment.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 *
 * http://camel.apache.org/spring-boot.html
 *
 * <br />
 * https://github.com/apache/camel/tree/master/examples/camel-example-spring-boot-starter
 *
 * <br />
 *
 * Created by wallace on 29/07/16.
 */
@Component
public class TestTimerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("timer:hello?period={{timer.period}}")
                .transform(method("cielo", "defaultMessage"))
                .log("${routeId} - ${body}")
                .to("mock:timer");
    }
}
