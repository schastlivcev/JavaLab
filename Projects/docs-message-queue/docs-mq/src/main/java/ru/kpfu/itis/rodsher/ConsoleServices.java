package ru.kpfu.itis.rodsher;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.ConnectionFactory;
import ru.kpfu.itis.rodsher.models.Identity;
import ru.kpfu.itis.rodsher.models.Passport;
import ru.kpfu.itis.rodsher.producers.IdentityProducer;
import ru.kpfu.itis.rodsher.producers.PassportProducer;
import ru.kpfu.itis.rodsher.producers.ReceiptProducer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class ConsoleServices {
    private static final String IDENTITY = "identity";
    private static final String PASSPORT = "passport";
    private static final String RECEIPT = "receipt";
    private static final String EXIT = "exit";

    public static void main(String[] args) throws ParseException, IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));

        IdentityProducer identityProducer = new IdentityProducer(connectionFactory, objectMapper);
        PassportProducer passportProducer = new PassportProducer(connectionFactory, objectMapper);
        ReceiptProducer receiptProducer = new ReceiptProducer(connectionFactory, objectMapper);

        Scanner sc = new Scanner(System.in);
        sc.useDelimiter("\n");

        System.out.println("Добро пожаловать в Консольные услуги Счастливцева");

        boolean end = false;
        while(!end) {
            System.out.println("Вы можете обновить данные электронного удостоверения(" + IDENTITY
                    + "), загрузить данные паспорта(" + PASSPORT + "), загрузить квитанции(" + RECEIPT
                    + ") или завершить сеанс(" + EXIT + ")");
            System.out.print("Выберите интересуемый раздел: ");
            String type = sc.next();
            try {
                switch (type) {
                    case IDENTITY:
                        getIdentity(sc, identityProducer);
                        break;
                    case PASSPORT:
                        System.out.println("--------------------------");
                        System.out.println("Загрузка паспорта: ");
                        System.out.print("Выберите способ: фото(image) или ввести данные(data): ");
                        String passType = sc.next();
                        switch (passType) {
                            case "image":
                                getPassportImage(sc, passportProducer);
                                break;
                            case "data":
                                getPassportData(sc, passportProducer);
                                break;
                        }
                        break;
                    case RECEIPT:
                        getReceipt(sc, receiptProducer);
                        break;
                    case EXIT:
                        System.out.println("Сеанс завершен.");
                        end = true;
                        System.exit(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void getReceipt(Scanner sc, ReceiptProducer receiptProducer) throws IOException {
        System.out.println("--------------------------------------");
        System.out.println("Загрузка квитанции: ");
        System.out.print("Введите ссылку на квитанцию: ");
        String url = sc.next();
        System.out.println("Квитанция загружена, ожидайте ответа.");
        receiptProducer.produce(url, UUID.randomUUID().toString());
    }

    private static void getIdentity(Scanner sc, IdentityProducer identityProducer) throws IOException, ParseException {
        System.out.println("--------------------------------------");
        System.out.println("Обновление электронного удостоверения: ");
        System.out.print("Введите фамилию: ");
        String surname = sc.next();
        System.out.print("Введите имя: ");
        String name = sc.next();
        System.out.print("Введите отчество: ");
        String middleName = sc.next();
        System.out.print("Введите пол(м/ж): ");
        boolean isMan = sc.next().equals("м");
        System.out.print("Введите дату рождения(дд.мм.гггг): ");
        Date birth = new SimpleDateFormat("dd.MM.yyyy").parse(sc.next());
        System.out.print("Введите адрес прописки(страна, город, улица, дом, квартира): ");
        String address = sc.next();
        System.out.print("Введите номер телефона(только цифры): ");
        long phone = sc.nextLong();
        System.out.print("Введите электронную почту: ");
        String email = sc.next();
        System.out.println("Ваши данные были обновлены, ожидайте удостоверения.");
        Identity identity = new Identity(surname, name, middleName, isMan, birth, address, phone, email);

        identityProducer.produce(identity);
    }

    private static void getPassportImage(Scanner sc, PassportProducer passportProducer) throws IOException {
        System.out.print("Введите ссылку на фото основной части: ");
        String urlMain = sc.next();
        System.out.print("Введите ссылку на фото прописки: ");
        String urlAdd = sc.next();
        System.out.println("Ваши данные были обновлены, ожидайте паспорт.");
        passportProducer.produceFromImage(urlMain, urlAdd, UUID.randomUUID().toString());
    }

    private static void getPassportData(Scanner sc, PassportProducer passportProducer) throws IOException, ParseException {
        System.out.print("Введите серию и номер паспорта(без пробелов): ");
        long series = sc.nextLong();
        System.out.print("Введите кем выдан: ");
        String issuedBy = sc.next();
        System.out.print("Введите код подразделения(только цифры): ");
        long issuedByCode = sc.nextLong();
        System.out.print("Введите дату выдачи: ");
        Date issueDate = new SimpleDateFormat("dd.MM.yyyy").parse(sc.next());
        System.out.print("Введите фамилию: ");
        String surname = sc.next();
        System.out.print("Введите имя: ");
        String name = sc.next();
        System.out.print("Введите отчество: ");
        String middleName = sc.next();
        System.out.print("Введите пол(м/ж): ");
        boolean isMan = sc.next().equals("м");
        System.out.print("Введите дату рождения(дд.мм.гггг): ");
        Date birth = new SimpleDateFormat("dd.MM.yyyy").parse(sc.next());
        System.out.print("Введите город: ");
        String city = sc.next();
        System.out.print("Введите адрес прописки(страна, город, улица, дом, квартира): ");
        String address = sc.next();
        System.out.println("Ваши данные были обновлены, ожидайте паспорт.");

        Passport passport = new Passport(surname, name, middleName, isMan, birth, city, address, series, issuedBy, issuedByCode, issueDate);
        passportProducer.produceFromData(passport);
    }
}
