package br.com.caelum.camel.durablesubscriber;

import javax.jms.*;
import javax.naming.InitialContext;
import java.util.UUID;

/**
 * Created by wallace on 02/05/16.
 */
public class TopicSubscriberSend {

    public static void main(String[] args) throws Exception {

        InitialContext context = new InitialContext();
        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
        Connection connection = factory.createConnection();
        Session session = connection.createSession(false, Session. AUTO_ACKNOWLEDGE);
        Topic topic = (Topic) context.lookup("NFE");
        MessageProducer producer =  session.createProducer(topic);
        TextMessage message = session.createTextMessage();
        message.setText("<pedido>" +
                "<numeroPedido>" + UUID.randomUUID().toString() + "</numeroPedido>" +
                "<valor>R$298,90</valor>" +
                "</pedido>");

        connection.start();
        //producer.setTimeToLive(60*1000);
        producer.send(topic, message);

        session.close();
        connection.close();
        producer.close();

    }
}
