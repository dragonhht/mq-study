package hht.dragon.activemq;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;

/**
 * Description.
 * User: huang
 * Date: 18-8-14
 */
public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:application-context.xml");
        Sender sender = (Sender) context.getBean("sender");
        sender.send("test-1", "hello");

        while (true) {

        }
    }

}
