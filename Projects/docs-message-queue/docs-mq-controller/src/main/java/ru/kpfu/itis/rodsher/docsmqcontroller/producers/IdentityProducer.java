package ru.kpfu.itis.rodsher.docsmqcontroller.producers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import ru.kpfu.itis.rodsher.docsmqcontroller.models.Identity;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class IdentityProducer {
    private static final String IDENTITY_FANOUT_EXCHANGE = "identity_fanout_exchange";

    private Connection connection;
    private Channel channel;
    private ObjectMapper objectMapper;

    public IdentityProducer(ConnectionFactory connectionFactory, ObjectMapper objectMapper) throws IOException, TimeoutException {
        this.objectMapper = objectMapper;
        connection = connectionFactory.newConnection();
        channel = connection.createChannel();

        channel.exchangeDeclare(IDENTITY_FANOUT_EXCHANGE, "fanout");
    }

    public void produce(Identity identity) throws IOException {
        channel.basicPublish(IDENTITY_FANOUT_EXCHANGE, "", null, objectMapper.writeValueAsBytes(identity));
    }
}
