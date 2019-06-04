package hht.dragon.activemq.pub.sub;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 发布者.
 *
 * @author: huang
 * @Date: 2019-6-4
 */
public class Publisher {

    public static void main(String[] args) throws JMSException, InterruptedException {
        ConnectionFactory factory = new ActiveMQConnectionFactory("dragonhht", "dragonhht",
                "tcp://localhost:61616");
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic("test-topic");
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        while (true) {
            TimeUnit.SECONDS.sleep(5);
            producer.send(session.createTextMessage("订阅发布测试数据: " + new Date()));
        }
    }

}
