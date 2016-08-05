package br.com.gateway.payment.domain;

import java.io.Serializable;

/**
 * Created by wallace on 29/07/16.
 */
public class Transaction implements Serializable {

    private String orderId;
    private String amount;
    private String softDescriptor;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSoftDescriptor() {
        return softDescriptor;
    }

    public void setSoftDescriptor(String softDescriptor) {
        this.softDescriptor = softDescriptor;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "orderId='" + orderId + '\'' +
                ", amount='" + amount + '\'' +
                ", softDescriptor='" + softDescriptor + '\'' +
                '}';
    }
}
