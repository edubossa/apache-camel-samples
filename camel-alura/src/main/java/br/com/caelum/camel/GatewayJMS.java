package br.com.caelum.camel;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by wallace on 20/04/16.
 */
public class GatewayJMS implements MessageListener {

    @Override
    public void onMessage(Message message) {

        try {
            TextMessage text = (TextMessage) message;
            System.out.println("JMS : " + text.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
