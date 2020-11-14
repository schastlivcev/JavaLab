package ru.kpfu.itis.rodsher.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Delivery;
import ru.kpfu.itis.rodsher.models.PersonalData;
import ru.kpfu.itis.rodsher.services.PdfCreator;

public class PromotionConsumer extends ExchangeConsumer {
    private ObjectMapper objectMapper;
    private PdfCreator pdfCreator;

    public PromotionConsumer() {
        super("personal_data_exchange", "fanout");
        objectMapper = new ObjectMapper();
        pdfCreator = new PdfCreator();
    }

    public static void main(String[] args) {
        PromotionConsumer promotionConsumer = new PromotionConsumer();
        promotionConsumer.start();
    }

    @Override
    public void consume(String consumerTag, Delivery message) {
        try {
            PersonalData personalData = objectMapper.readValue(new String(message.getBody()), PersonalData.class);
            if(pdfCreator.createPromotion(personalData)) {
                System.out.println("Created PROMOTION for " + personalData.getSurname() + " " + personalData.getPassport().getSeries());
            } else {
                System.out.println("Failed to create PROMOTION for " + personalData.getSurname() + " " + personalData.getPassport().getSeries());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
