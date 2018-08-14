package hht.dragon.activemq;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;

import javax.jms.Queue;

// 开启JMS
@EnableJms
@SpringBootApplication
public class SpringBootActivemqApplication {

	/**
	 * 定义队列.
	 * @return
	 */
	@Bean
	public Queue queue() {
		return new ActiveMQQueue("spring-boot-test");
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootActivemqApplication.class, args);
	}
}
