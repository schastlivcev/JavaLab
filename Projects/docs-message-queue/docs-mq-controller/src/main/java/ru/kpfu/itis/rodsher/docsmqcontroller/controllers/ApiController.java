package ru.kpfu.itis.rodsher.docsmqcontroller.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.rodsher.docsmqcontroller.models.Identity;
import ru.kpfu.itis.rodsher.docsmqcontroller.models.Passport;
import ru.kpfu.itis.rodsher.docsmqcontroller.producers.IdentityProducer;
import ru.kpfu.itis.rodsher.docsmqcontroller.producers.PassportProducer;
import ru.kpfu.itis.rodsher.docsmqcontroller.producers.ReceiptProducer;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/produce")
public class ApiController {
    private static int identityRequests = 0;
    private static int receiptRequests = 0;
    private static int passportRequests = 0;

    @Autowired
    private IdentityProducer identityProducer;
    @Autowired
    private PassportProducer passportProducer;
    @Autowired
    private ReceiptProducer receiptProducer;
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/identity")
    public ResponseEntity postIdentity(@RequestBody String body) throws IOException {
        Identity identity = objectMapper.readValue(body, Identity.class);
        identityProducer.produce(identity);
        identityRequests++;
        System.out.println("Identity Request #" + identityRequests);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/receipt")
    public ResponseEntity postReceipt(@RequestBody String body) throws IOException {
        receiptProducer.produce(body, UUID.randomUUID().toString());
        receiptRequests++;
        System.out.println("Receipt Request #" + receiptRequests);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/passport/data")
    public ResponseEntity postPassportData(@RequestBody String body) throws IOException {
        Passport passport = objectMapper.readValue(body, Passport.class);
        passportProducer.produceFromData(passport);
        passportRequests++;
        System.out.println("PASSPORT " +  passportRequests + ": " + passport);
        System.out.println("Passport Request #" + passportRequests);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/passport/image")
    public ResponseEntity postPassportImage(@RequestBody String body) throws IOException {
        String[] strings = body.split(" ");
        passportProducer.produceFromImage(strings[0], strings[1], UUID.randomUUID().toString());
        passportRequests++;
        System.out.println("Passport Request #" + passportRequests);
        return ResponseEntity.ok().build();
    }
}
