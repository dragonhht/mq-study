package hht.dragon.activemq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Queue;

/**
 * Description.
 * User: huang
 * Date: 18-8-14
 */
@RestController
public class ActiveMQController {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private Queue queue;

    /**
     * 生产者.
     * @param message
     */
    @RequestMapping("/send")
    public void sender(String message) {
        this.jmsMessagingTemplate.convertAndSend(queue, message);
    }
}
