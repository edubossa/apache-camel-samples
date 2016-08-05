package br.com.gateway;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;

/**
 * Represents a Gateway endpoint.
 */
@UriEndpoint(scheme = "gateway", title = "Gateway", syntax="gateway:acquirer", consumerClass = GatewayConsumer.class, label = "Gateway")
public class GatewayEndpoint extends DefaultEndpoint {

    @UriParam
    private GatewayConfiguration configuration;

    public GatewayEndpoint() {}

    public GatewayEndpoint(String uri, GatewayComponent component, GatewayConfiguration configuration) {
        super(uri, component);
        this.configuration = configuration;
    }

    public GatewayEndpoint(String endpointUri) {
        super(endpointUri);
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        System.out.println("GatewayEndpoint.doStart");
    }

    public Producer createProducer() throws Exception {
        return new GatewayProducer(this);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        throw new UnsupportedOperationException("You cannot receive messages from this endpoint");
    }

    public boolean isSingleton() {
        return true;
    }

    public GatewayConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(GatewayConfiguration configuration) {
        this.configuration = configuration;
    }

}