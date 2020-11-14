package ru.kpfu.itis.rodsher.producers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import ru.kpfu.itis.rodsher.models.MiniPassport;
import ru.kpfu.itis.rodsher.models.PersonalData;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class PersonalDataProducer {
    private final static String EXCHANGE_NAME = "personal_data_exchange";
    private final static String EXCHANGE_TYPE = "fanout";

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        try {
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);

            boolean end = false;
            System.out.println("Welcome to Personal Data Producer!");
            Scanner sc = new Scanner(System.in);
            System.out.print("Start?(y/n): ");
            String again = sc.next();
            if(again.toLowerCase().equals("n")) {
                end = true;
            } else {
                System.out.println();
            }
            while(!end) {
                System.out.print("Enter surname: ");
                String surname = sc.next();
                System.out.print("Enter name: ");
                String name = sc.next();
                System.out.print("Enter middle name: ");
                String middleName = sc.next();
                System.out.print("Enter age: ");
                int age = sc.nextInt();
                System.out.print("Enter passport series(without spaces): ");
                long passportSeries = sc.nextLong();
                System.out.print("Enter passport issue date(dd.mm.yyyy): ");
                Date passportIssueDate = new SimpleDateFormat("dd.MM.yyyy").parse(sc.next());
                ObjectMapper objectMapper = new ObjectMapper();
                String result = objectMapper.writeValueAsString(
                        new PersonalData(name, surname, middleName, age, new MiniPassport(passportSeries, passportIssueDate)));
                channel.basicPublish(EXCHANGE_NAME, "", null, result.getBytes());
                System.out.println("Your result: " + result);
                System.out.println();
                System.out.print("Try again?(y/n): ");
                again = sc.next();
                if(again.toLowerCase().equals("n")) {
                    end = true;
                } else {
                    System.out.println();
                }
            }
            connection.close();
        } catch (IOException | TimeoutException | ParseException e) {
            e.printStackTrace();
        }
    }
}
