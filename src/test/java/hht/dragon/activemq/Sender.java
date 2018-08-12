package hht.dragon.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;

/**
 * 消息发送者.
 * User: huang
 * Date: 18-8-12
 */
public class Sender {

    /**
     * 消息生产者.
     * @throws JMSException
     */
    @Test
    public void sender() throws JMSException {
        // 建立ConnectionFactory工厂对象,使用默认的用户名及密码
        ConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_USER,
                ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                "tcp://localhost:61616");
        // 通过ConnectionFactory创建Connection,并通过start开启连接
        Connection connection = factory.createConnection();
        connection.start();
        // 通过Connection创建Session，用于接收消息，参数为（是否启动事务，签收模式），一般使用自动签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 通过Session创建Destination，用于指定生产消息目标和消费消息来源(点对点中为queue, 订阅/发布中为Topic)
        Destination destination = session.createQueue("test-queue");
        // 通过Session创建消息的生产者(MessageProducer)和消费者(MessageConsumer)
        MessageProducer producer = session.createProducer(destination);
        // 通过MessageProducer的setDeliveryMode()设置持久化特性及非持久化特性
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        // 创建并发送数据
        for (int i = 0; i < 10; i++) {
            TextMessage message = session.createTextMessage("测试发送者消息内容: " + i);
            producer.send(message);
        }

        // 释放连接
        connection.close();
    }

    /**
     * 消息消费者.
     * @throws JMSException
     */
    @Test
    public void consumer() throws JMSException {
        // 建立ConnectionFactory工厂对象,使用默认的用户名及密码
        ConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_USER,
                ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                "tcp://localhost:61616");
        // 通过ConnectionFactory创建Connection,并通过start开启连接
        Connection connection = factory.createConnection();
        connection.start();
        // 通过Connection创建Session，用于接收消息，参数为（是否启动事务，签收模式），一般使用自动签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 通过Session创建Destination，用于指定生产消息目标和消费消息来源(点对点中为queue, 订阅/发布中为Topic)
        Destination destination = session.createQueue("test-queue");
        // 通过Session创建消息的生产者(MessageProducer)和消费者(MessageConsumer)
        MessageConsumer consumer = session.createConsumer(destination);

        // 获取消息
        while (true) {
            TextMessage message = (TextMessage)consumer.receive();
            if (message == null) break;
            System.out.println(message.getText());
        }

        // 释放连接
        connection.close();
    }

}
