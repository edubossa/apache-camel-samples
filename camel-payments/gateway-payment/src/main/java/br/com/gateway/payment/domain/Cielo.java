package br.com.gateway.payment.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by wallace on 29/07/16.
 */
@Component
public class Cielo {

    @Value("${msg.default}")
    private String msg;

    public String defaultMessage() {
        return msg;
    }

}
