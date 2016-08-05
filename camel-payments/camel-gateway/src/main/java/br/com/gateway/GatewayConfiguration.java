package br.com.gateway;

import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriParams;

/**
 * Created by wallace on 04/08/16.
 */

@UriParams
public class GatewayConfiguration {

    @UriParam @Metadata(required = "true")
    private String acquirer;

    @UriParam @Metadata(required = "true")
    private String type;

    @UriParam
    private String version;

    @UriParam(defaultValue = "1")
    private int retry = 1;

    public GatewayConfiguration(String acquirer) {
        this.acquirer = acquirer;
    }

    public String getAcquirer() {
        return acquirer;
    }

    public void setAcquirer(String acquirer) {
        this.acquirer = acquirer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }
}
