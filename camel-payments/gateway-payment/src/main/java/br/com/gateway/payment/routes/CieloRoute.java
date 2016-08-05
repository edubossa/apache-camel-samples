package br.com.gateway.payment.routes;

import br.com.gateway.payment.domain.CieloMessage;
import br.com.gateway.payment.domain.Transaction;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.gson.GsonDataFormat;
import org.apache.camel.spi.DataFormat;
import org.springframework.stereotype.Component;

/**
 * Created by wallace on 29/07/16.
 */
@Component
public class CieloRoute extends RouteBuilder {

    DataFormat format = new GsonDataFormat(Transaction.class);

    @Override
    public void configure() throws Exception {

        from("jms:queue:cielo")
            .unmarshal(format)
            .bean(CieloMessage.class, "onMessage")
            //.log("${routeId} - ${body}")
        .to("mock:jms");

    }
}
