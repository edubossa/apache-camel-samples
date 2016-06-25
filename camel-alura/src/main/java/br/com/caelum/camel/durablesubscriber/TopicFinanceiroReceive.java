package br.com.caelum.camel.durablesubscriber;

import javax.jms.*;
import javax.naming.InitialContext;

/**
 * Created by wallace on 02/05/16.
 */
public class TopicFinanceiroReceive {

    public static void main(String[] args) throws Exception {

        InitialContext context = new InitialContext();
        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
        Connection connection = factory.createConnection();
        connection.setClientID("NTK-009897"); //identificacao da conexao
        Session session = connection.createSession(false, Session. AUTO_ACKNOWLEDGE);
        Topic topic = (Topic) context.lookup("NFE");
        MessageConsumer consumer = session.createDurableSubscriber(topic, "nfe-financeiro"); //topic |  identificacao da assinatura
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println("TopicFinanceiroReceive.onMessage: " + textMessage.getText() );
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        connection.start();

        System.in.read();
        consumer.close();
        session.close();
        connection.close();

    }
}
