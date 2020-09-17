package ru.kpfu.itis.rodsher.consumers;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public abstract class ExchangeConsumer {
    private final String EXCHANGE_NAME;
    private final String EXCHANGE_TYPE;

    public ExchangeConsumer(String exchangeName, String exchangeType) {
        EXCHANGE_NAME = exchangeName;
        EXCHANGE_TYPE = exchangeType;
    }

    public abstract void consume(String consumerTag, Delivery message);

    public void start() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        try {
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);

            String queue = channel.queueDeclare().getQueue();
            channel.queueBind(queue, EXCHANGE_NAME, "");

            DeliverCallback deliverCallback = this::consume;
            channel.basicConsume(queue, true, deliverCallback, consumerTag -> {});
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
