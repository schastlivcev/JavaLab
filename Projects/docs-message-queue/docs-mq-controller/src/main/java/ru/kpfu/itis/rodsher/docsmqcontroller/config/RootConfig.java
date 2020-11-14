package ru.kpfu.itis.rodsher.docsmqcontroller.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.kpfu.itis.rodsher.docsmqcontroller.producers.IdentityProducer;
import ru.kpfu.itis.rodsher.docsmqcontroller.producers.PassportProducer;
import ru.kpfu.itis.rodsher.docsmqcontroller.producers.ReceiptProducer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Configuration
@ComponentScan("ru.kpfu.itis.rodsher.docsmqcontroller")
public class RootConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        return connectionFactory;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        return objectMapper;
    }

    @Bean
    public IdentityProducer identityProducer(ConnectionFactory connectionFactory, ObjectMapper objectMapper) throws IOException, TimeoutException {
        return new IdentityProducer(connectionFactory, objectMapper);
    }

    @Bean
    public PassportProducer passportProducer(ConnectionFactory connectionFactory, ObjectMapper objectMapper) throws IOException, TimeoutException {
        return new PassportProducer(connectionFactory, objectMapper);
    }

    @Bean
    public ReceiptProducer receiptProducer(ConnectionFactory connectionFactory, ObjectMapper objectMapper) throws IOException, TimeoutException {
        return new ReceiptProducer(connectionFactory, objectMapper);
    }
}
