package br.com.gateway.payment.domain;


/**
 * Created by wallace on 29/07/16.
 */
public class CieloMessage {

    public void onMessage(Transaction transaction) {

        System.out.println(transaction.toString());
    }

}
