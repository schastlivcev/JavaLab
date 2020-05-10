package ru.kpfu.itis.rodsher.jlmq;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

public class App {
    public static void main( String[] args ) throws URISyntaxException, InterruptedException {
        Connector connector = Jlmq.connector().onUri(new URI("ws://localhost:8080/jlmq")).connect();
        Producer producer = connector.producer().toQueue("Println").build();
        producer.createTask(Collections.singletonMap("5", 789));
        Consumer consumer = connector.consumer().subscribeTo("Println").onReceive(message -> {
            System.out.println("payload: " + message.getPayload());
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.out.println("INTERRUPTED");
            }
        }).build();
        Thread.sleep(50000);
        connector.disconnect();
    }
}
