package ru.kpfu.itis.rodsher.consumers;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Delivery;
import ru.kpfu.itis.rodsher.models.Identity;
import ru.kpfu.itis.rodsher.services.TemplatePdfCreator;

import java.io.UnsupportedEncodingException;

public class PassLevelCConsumer extends ExchangeConsumer {
    private static final String IDENTITY_FANOUT_EXCHANGE = "identity_fanout_exchange";

    private ObjectMapper objectMapper;
    private TemplatePdfCreator pdfCreator;

    public PassLevelCConsumer() {
        super(IDENTITY_FANOUT_EXCHANGE, "fanout");
        objectMapper = new ObjectMapper();
        objectMapper.setVisibility(objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        pdfCreator = new TemplatePdfCreator();
    }

    public static void main(String[] args) {
        PassLevelCConsumer passLevelCConsumer = new PassLevelCConsumer();
        passLevelCConsumer.start();
    }

    @Override
    public void consume(String consumerTag, Delivery message) {
        try {
            String contentType = message.getProperties().getContentType();
            String charset = contentType != null && contentType.contains("CP1251") ? "CP1251" : "UTF-8";
            Identity identity = objectMapper.readValue(new String(message.getBody(), charset), Identity.class);
            if(pdfCreator.createPassLevelC(identity, "Сидиков M. P.", null)) {
                System.out.println("Created PASS_LEVEL_C for " + identity.getSurname() + " " + identity.getName() + ", " + identity.getPhone());
            } else {
                System.out.println("Failed to create PASS_LEVEL_C for " + identity.getSurname() + " " + identity.getName() + ", " + identity.getPhone());
            }
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
