package hht.dragon.activemq;

import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 消费者.
 * User: huang
 * Date: 18-8-14
 */
@Component
public class QueueReceiver implements MessageListener {
    @Override
    public void onMessage(Message message) {
        try {
            System.out.println("收到消息: " + ((TextMessage)message).getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
