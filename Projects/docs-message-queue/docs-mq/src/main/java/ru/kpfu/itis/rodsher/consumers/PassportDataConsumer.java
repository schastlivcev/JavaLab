package ru.kpfu.itis.rodsher.consumers;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Delivery;
import ru.kpfu.itis.rodsher.models.Passport;
import ru.kpfu.itis.rodsher.services.TemplatePdfCreator;

import java.io.UnsupportedEncodingException;

public class PassportDataConsumer extends QueueConsumer {
    private static final String PASSPORT_DATA_QUEUE = "passport_data_queue";

    private ObjectMapper objectMapper;
    private TemplatePdfCreator pdfCreator;

    public PassportDataConsumer() {
        super(PASSPORT_DATA_QUEUE);
        objectMapper = new ObjectMapper();
        objectMapper.setVisibility(objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        pdfCreator = new TemplatePdfCreator();
    }

    public static void main(String[] args) {
        PassportDataConsumer passportDataConsumer = new PassportDataConsumer();
        passportDataConsumer.start();
    }

    @Override
    public void consume(String consumerTag, Delivery message) {
        try {
            String contentType = message.getProperties().getContentType();
            String charset = contentType != null && contentType.contains("CP1251") ? "CP1251" : "UTF-8";
            Passport passport = objectMapper.readValue(new String(message.getBody(), charset), Passport.class);
            if(pdfCreator.createPassportFromData(passport, null)) {
                System.out.println("Created PASSPORT_DATA for " + passport.getSurname() + " " + passport.getName() + ", " + passport.getSeries());
            } else {
                System.out.println("Failed to create PASSPORT_DATA for " + passport.getSurname() + " " + passport.getName() + ", " + passport.getSeries());
            }
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
