package ru.kpfu.itis.rodsher.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import ru.kpfu.itis.rodsher.models.PersonalData;
import ru.kpfu.itis.rodsher.services.PdfCreator;

public class ResignationConsumer extends ExchangeConsumer {
    private ObjectMapper objectMapper;
    private PdfCreator pdfCreator;

    public ResignationConsumer() {
        super("personal_data_exchange", "fanout");
        objectMapper = new ObjectMapper();
        pdfCreator = new PdfCreator();
    }

    public static void main(String[] args) {
        ResignationConsumer resignationConsumer = new ResignationConsumer();
        resignationConsumer.start();
    }

    @Override
    public void consume(String consumerTag, Delivery message) {
        try {
            PersonalData personalData = objectMapper.readValue(new String(message.getBody()), PersonalData.class);
            if(pdfCreator.createResignation(personalData)) {
                System.out.println("Created RESIGNATION for " + personalData.getSurname() + " " + personalData.getPassport().getSeries());
            } else {
                System.out.println("Failed to create RESIGNATION for " + personalData.getSurname() + " " + personalData.getPassport().getSeries());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
