package ru.kpfu.itis.rodsher.docsmqcontroller.producers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import ru.kpfu.itis.rodsher.docsmqcontroller.models.Passport;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class PassportProducer {
    private static final String PASSPORT_DIRECT_EXCHANGE = "passport_direct_exchange";

    private static final String PASSPORT_DATA_ROUTING_KEY = "data";
    private static final String PASSPORT_IMAGE_ROUTING_KEY = "image";
    private static final String PASSPORT_DATA_QUEUE = "passport_data_queue";
    private static final String PASSPORT_IMAGE_QUEUE = "passport_image_queue";

    private Connection connection;
    private Channel channel;
    private ObjectMapper objectMapper;

    public PassportProducer(ConnectionFactory connectionFactory, ObjectMapper objectMapper) throws IOException, TimeoutException {
        this.objectMapper = objectMapper;
        connection = connectionFactory.newConnection();
        channel = connection.createChannel();

        channel.exchangeDeclare(PASSPORT_DIRECT_EXCHANGE, "direct");
        channel.queueBind(PASSPORT_DATA_QUEUE, PASSPORT_DIRECT_EXCHANGE, PASSPORT_DATA_ROUTING_KEY);
        channel.queueBind(PASSPORT_IMAGE_QUEUE, PASSPORT_DIRECT_EXCHANGE, PASSPORT_IMAGE_ROUTING_KEY);
    }

    public void produceFromData(Passport passport) throws IOException {
        channel.basicPublish(PASSPORT_DIRECT_EXCHANGE, PASSPORT_DATA_ROUTING_KEY, null, objectMapper.writeValueAsBytes(passport));
    }

    public void produceFromImage(String urlMain, String urlAdd, String id) throws IOException {
        String url = urlMain + " " + urlAdd + " " + id;
        channel.basicPublish(PASSPORT_DIRECT_EXCHANGE, PASSPORT_IMAGE_ROUTING_KEY, null, url.getBytes());
    }
}