package hht.dragon.activemq.pub.sub;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.concurrent.TimeUnit;

/**
 * 订阅者.
 *
 * @author: huang
 * @Date: 2019-6-4
 */
public class Subscriber {

    public static void main(String[] args) throws JMSException, InterruptedException {
        ConnectionFactory factory = new ActiveMQConnectionFactory("dragonhht", "dragonhht",
                "tcp://localhost:61616");
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic("test-topic");
        MessageConsumer consumer = session.createConsumer(destination);
        /*while (true) {
            TextMessage message = (TextMessage)consumer.receive();
            if (message == null) break;
            System.out.println(message.getText());
        }*/

        // 使用监听
        consumer.setMessageListener(message -> {
            if (message instanceof TextMessage) {
                try {
                    System.out.println(((TextMessage) message).getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        TimeUnit.MINUTES.sleep(3);
        // 释放连接
        connection.close();
    }

}
