package ru.kpfu.itis.rodsher.jlmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.MimeTypeUtils;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

public class App {
    public static void main( String[] args ) throws URISyntaxException, InterruptedException, JsonProcessingException {
        StompConnector connector = Jlmq.connector().onUri(new URI("ws://localhost:8080/jlmq")).connect();
        Producer producer = connector.producer().toQueue("Println").build();
        producer.createTask(Collections.singletonMap("54321", 789));
        Consumer consumer = connector.consumer().subscribeTo("Println").onReceive((payload, headers) -> {
            System.out.println("payload: " + payload + ", headers: " + headers);
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
