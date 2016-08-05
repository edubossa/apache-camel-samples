package br.com.gateway.payment.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by wallace on 30/07/16.
 */
@Component
public class PaymentRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("jms:queue:payment")

            .choice()
                .when(header("acquirer").isEqualToIgnoreCase("cielo"))
                    .to("gateway://cielo?type=Credit&version=API3.0&retry=5")
                    .to("direct:payment")

                .when(header("acquirer").isEqualToIgnoreCase("redecard"))
                    .to("gateway://redecard?type=Debit&version=V3.5")
                    .to("direct:payment")

                .when(header("acquirer").isEqualToIgnoreCase("elavon"))
                    .to("gateway://elavon?type=Debit&version=V1.1")
                    .to("direct:payment")

                .when(header("acquirer").isEqualToIgnoreCase("getnet"))
                    .to("gateway://getnet?type=Debit&version=V2.3")
                    .to("direct:payment")

                .otherwise()
                    .log("ROTA NAO LOCALIZADA - ${body}")
                .stop()

             .end();
         ;

        from("direct:payment")
            .process(new Processor() {
                @Override
                public void process(Exchange exchange) throws Exception {
                    System.out.println("Acquirer :" + exchange.getIn().getHeader("acquirer"));
                    System.out.println(exchange.getIn().getBody().toString());
                }
            })
        ;

    }
}
