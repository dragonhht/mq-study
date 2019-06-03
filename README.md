# ActiveMQ学习

-   相关依赖

```
    <dependency>
      <groupId>org.apache.activemq</groupId>
      <artifactId>activemq-all</artifactId>
      <version>5.15.5</version>
    </dependency>
```

## [ActiveMQ初体验](./src/test/java/hht/dragon/activemq/Sender.java)

-   生产者
    
    ```
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
    ```
    
-   消费者

    ```
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
    ```
    
## 1、安全机制

-   ActiveMQ的管理界面默认为：`http://ip:8161/admin`，可在`conf/jetty.xml`文件中修改端口,在文件`conf/jetty-realm.properties`中定义用户名及密码

-   可在文件`conf/activemq.xml`中添加安全验证配置，只有符合认证的用户才可发送及获取消息，如：

    ```
    	<!-- 安全验证 -->
    	<plugins> 
    	     <simpleAuthenticationPlugin> 
    		    <!--配置发送或接受的用户名和密码,以及所处的用户组-->
    		    <users> 
    		      <authenticationUser username="dragonhht" password="dragonhht" groups="users,admins"/>
    		    </users> 
    	    </simpleAuthenticationPlugin>
    	</plugins>
    ```
    > 并在发送及接收消息时使用该认证用户：
    
    ```
    ConnectionFactory factory = new ActiveMQConnectionFactory("dragonhht", "dragonhht", "tcp://localhost:61616");
    ```
 
 # 2、ActiveMQ的签收方式
 
 -  `Session.AUTO_ACKNOWLEDGE`: 当客户端从receive或onMessage成功返回时，Session自动签收 客户端的这条信息的收条
 
 -  `Session.CLIENT_ACKNOWLEDGE`(推荐使用): 客户端通过调用消息的`acknowledge`方法签收信息， 如在接收到消息后：
 
    ```
    TextMessage message = (TextMessage)consumer.receive();
    message.acknowledge();
    ```
 
 -  `Session.DUPS_OK_ACKNOWLEDGE`: 指示Session不必确保对消息进行签收（可能一起重复消息）
 
 # 3、事务设置
 
 ## 事务性会话
 
-   当创建会话时`connection.createSession(true, Session.AUTO_ACKNOWLEDGE)`传入`true`时，将使用事务性会话。启用事务后，发送者需在发送完消息后提交事务: `session.commit();`

## 非事务性会话

-   当创建会话时`connection.createSession(false, Session.AUTO_ACKNOWLEDGE)`传入`false`时,将使用第二个参数所设置的非事务性会话模式

    -   `AUTO_ACKNOWLEDGE`: 当客户端成功从recive方法返回以后, 会话会自动确认该消息
    
    -   `CLIENT_ACKNOWLEDGE`: 客户端通过调用消息的`textMessage.acknowledge();`确认消息。在该模式下，若客户端循环接收消息，但只在第n个消息才确认时，则前面的n个消息将全部确认
    
    -   `DUPS_OK_ACKNOWLEDGE`: 延迟确认
 
 # 4、Spring 整合ActiveMQ
 
 -  引入相关依赖
 
 ```
        <!--activeMQ-->
        <dependency>
            <groupId>org.apache.activemq</groupId>
            <artifactId>activemq-all</artifactId>
            <version>5.15.5</version>
        </dependency>
        <!-- Spring整合ActiveMQ所需依赖 -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jms</artifactId>
            <version>${springversion}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-messaging</artifactId>
            <version>${springversion}</version>
        </dependency>
```
 
 -  对ActiveMQ进行配置
 
 ```
    <!-- ActiveMQ 连接工厂 -->
    <amq:connectionFactory id="amqConnectionFactory" brokerURL="tcp://localhost:61616" userName="dragonhht" password="dragonhht" />
    <!-- Spring Caching连接工厂 -->
    <bean id="connectionFactory" class="org.springframework.jms.connection.CachingConnectionFactory">
        <!--Connection的ConnectionFactory-->
        <property name="targetConnectionFactory" ref="amqConnectionFactory" />
        <!--Session缓存数-->
        <property name="sessionCacheSize" value="100" />
    </bean>

    <!--JmsTemplate-->
    <bean id="jmsTemplate" class="org.springframework.jms.core.JmsTemplate">
        <constructor-arg ref="connectionFactory" />
        <!--定义是否为pub/sub模型（发布/订阅），false为非pub/sub模型（发布/订阅）模式，即队列模式-->
        <property name="pubSubDomain" value="false" />
    </bean>

    <!--定义消费者-->
    <bean id="queueReceiver" class="hht.dragon.activemq.QueueReceiver"></bean>

    <!--定义监听器-->
    <!--destination-type属性确定是队列还是主题-->
    <jms:listener-container destination-type="queue" container-type="default" connection-factory="connectionFactory" acknowledge="auto">
        <jms:listener destination="test-1" ref="queueReceiver" />
    </jms:listener-container>
```
 
 -  生产者
 
 ```
@Component
public class Sender {

    @Autowired
    @Qualifier("jmsTemplate")
    private JmsTemplate jmsTemplate;

    public void send(String queueName, final String message) {
        jmsTemplate.send(queueName, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(message);
            }
        });
    }

}
```
 
 -  消费者（通过监听）
 
 ```
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
```

# 5、Spring Boot 整合ActiveMQ

-   引入相关依赖

```
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-activemq</artifactId>
		</dependency>
```

-   配置ActiveMQ

```
spring:
  activemq:
    broker-url: tcp://localhost:61616
    user: dragonhht
    password: dragonhht
    in-memory: false
    pool:
      enabled: false
```

-   在启动类中使用注解`@EnableJms`开启JMS

-   定义队列

```
	@Bean
	public Queue queue() {
		return new ActiveMQQueue("spring-boot-test");
	}
```

-   生产者(此处在Controller中写，方便产生消息)

```
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
```

-   消费者

```
@Component
public class Receiver {

    // 监听队列
    @JmsListener(destination = "spring-boot-test")
    public void receiver(String text) {
        System.out.println("接收到： " + text);
    }

}
```
