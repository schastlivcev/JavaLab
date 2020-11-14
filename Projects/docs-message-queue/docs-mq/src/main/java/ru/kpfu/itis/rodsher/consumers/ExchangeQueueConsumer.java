package ru.kpfu.itis.rodsher.consumers;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public abstract class ExchangeQueueConsumer {
    private final String EXCHANGE_NAME;
    private final String ROUTING_KEY;

    public ExchangeQueueConsumer(String exchangeName, String routingKey) {
        EXCHANGE_NAME = exchangeName;
        ROUTING_KEY = routingKey;
    }

    public abstract void consume(String consumerTag, Delivery message);

    public void start() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        try {
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();

            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, ROUTING_KEY);

            DeliverCallback deliverCallback = this::consume;
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
