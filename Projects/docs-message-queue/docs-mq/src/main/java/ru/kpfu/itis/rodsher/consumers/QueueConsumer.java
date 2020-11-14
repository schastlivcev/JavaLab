package ru.kpfu.itis.rodsher.consumers;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public abstract class QueueConsumer {
    private final String QUEUE_NAME;

    public QueueConsumer(String queueName) {
        QUEUE_NAME = queueName;
    }

    public abstract void consume(String consumerTag, Delivery message);

    public void start() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        try {
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();

            DeliverCallback deliverCallback = this::consume;
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
