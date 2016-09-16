package com.rabbitmq.action;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.config.ReceiverConfig;

public class ReceiveAction {

	
	public static void main(String[] args) {
		
		AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext(ReceiverConfig.class);

		final QueueingConsumer consumer = context.getBean(QueueingConsumer.class);
		
		new Thread() {

			@Override
			public void run() {
				try {
					while(true){
						System.out.println("接受数据");
						Delivery delivery = consumer.nextDelivery();
						String message=new String(delivery.getBody());
						System.out.println("接收到的数据为："+message);
						consumer.handleRecoverOk(consumer.getConsumerTag());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}.start();
	}

}
