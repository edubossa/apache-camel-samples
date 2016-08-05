package br.com.gateway;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Gateway producer.
 */
public class GatewayProducer extends DefaultProducer {
    private static final Logger LOG = LoggerFactory.getLogger(GatewayProducer.class);
    private GatewayEndpoint endpoint;

    public GatewayProducer(GatewayEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) throws Exception {
        LOG.warn("GatewayProducer.process");
        LOG.debug(exchange.getIn().getBody().toString());
        exchange.getOut().setBody("PAYMENT Eduardo Wallace! <<<<<<<< Gate@2All >>>>>>>>");
        exchange.getOut().setHeader("acquirer", endpoint.getConfiguration().getAcquirer());
    }

}
