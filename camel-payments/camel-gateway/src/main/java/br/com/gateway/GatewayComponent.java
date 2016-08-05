package br.com.gateway;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;

import java.util.Map;

/**
 * Represents the component that manages {@link GatewayEndpoint}.
 */
public class GatewayComponent extends UriEndpointComponent {
    
    public GatewayComponent() {
        super(GatewayEndpoint.class);
    }

    public GatewayComponent(CamelContext context) {
        super(context, GatewayEndpoint.class);
    }

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        GatewayConfiguration configuration = new GatewayConfiguration(remaining);
        setProperties(configuration, parameters);

        if (remaining == null || remaining.trim().length() == 0) {
            throw new IllegalArgumentException("acquirer must be specified.");
        }

        if (configuration.getType() == null) {
            throw new IllegalArgumentException("Type payment must be specified.");
        }

        return new GatewayEndpoint(uri, this, configuration);
    }

}
