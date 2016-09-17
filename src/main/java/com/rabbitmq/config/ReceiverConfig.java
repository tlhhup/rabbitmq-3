package com.rabbitmq.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

@Configuration
public class ReceiverConfig {
	
	@Value("${user}")
	private String userName;
	
	@Value("${password}")
	private String password;
	
	@Value("${hostName}")
	private String hostName;
	
	@Value("${portNumber}")
	private int portNumber;
	
	@Value("${queueName}")
	private String queueName;
	
	@Value("${exchangeName}")
	private String exchangeName;
	
	@Value("${routekey}")
	private String routekey;

	//分离配置文件
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
		PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
		org.springframework.core.io.Resource properties = new ClassPathResource("config.properties", ReceiverConfig.class.getClassLoader());
		propertySourcesPlaceholderConfigurer.setLocation(properties);
		return propertySourcesPlaceholderConfigurer;
	}

	//定义接受者
	@Bean
	QueueingConsumer queueingConsumer() throws Exception{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(hostName);
		factory.setUsername(userName);
		factory.setPassword(password);
		factory.setPort(portNumber);
		//创建连接
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		//声明exchange
		channel.exchangeDeclare(exchangeName, "direct",true);
		//申明队列
		channel.queueDeclare(queueName, true, false, false, null);
		channel.queueBind(queueName, exchangeName, routekey);
		QueueingConsumer consumer = new QueueingConsumer(channel);
		//接受指定消息队列中的数据
		channel.basicConsume(queueName, true, consumer);
		return consumer;
	}
	
}
