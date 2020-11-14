package ru.kpfu.itis.rodsher.docsmqcontroller.producers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeoutException;

public class ReceiptProducer {
    private static final String RECEIPT_TOPIC_EXCHANGE = "receipt_topic_exchange";

    private static final String RECEIPT_DOC_ROUTING_KEY = "files.document.doc";
    private static final String RECEIPT_PDF_ROUTING_KEY = "files.document.pdf";
    private static final String RECEIPT_PNG_ROUTING_KEY = "files.image.png";
    private static final String RECEIPT_JPG_ROUTING_KEY = "files.image.jpg";

    private Connection connection;
    private Channel channel;
    private ObjectMapper objectMapper;

    public ReceiptProducer(ConnectionFactory connectionFactory, ObjectMapper objectMapper) throws IOException, TimeoutException {
        this.objectMapper = objectMapper;
        connection = connectionFactory.newConnection();
        channel = connection.createChannel();

        channel.exchangeDeclare(RECEIPT_TOPIC_EXCHANGE, "topic");
    }

    public void produce(String fileUrl, String id) throws IOException {
        URL url = new URL(fileUrl);
        String fileExt = url.getFile().substring(url.getFile().lastIndexOf('.') + 1);
        String routing = "";
        switch (fileExt) {
            case "jpeg":
            case "jpg":
                routing = RECEIPT_JPG_ROUTING_KEY;
                break;
            case "png":
                routing = RECEIPT_PNG_ROUTING_KEY;
                break;
            case "pdf":
                routing = RECEIPT_PDF_ROUTING_KEY;
                break;
            case "doc":
            case "docx":
                routing = RECEIPT_DOC_ROUTING_KEY;
                break;
        }
        channel.basicPublish(RECEIPT_TOPIC_EXCHANGE, routing, null, (fileUrl + " " + id).getBytes());
    }
}