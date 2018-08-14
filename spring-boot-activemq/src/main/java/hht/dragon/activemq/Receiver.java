package hht.dragon.activemq;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * 消费者.
 * User: huang
 * Date: 18-8-14
 */
@Component
public class Receiver {

    // 监听队列
    @JmsListener(destination = "spring-boot-test")
    public void receiver(String text) {
        System.out.println("接收到： " + text);
    }

}
