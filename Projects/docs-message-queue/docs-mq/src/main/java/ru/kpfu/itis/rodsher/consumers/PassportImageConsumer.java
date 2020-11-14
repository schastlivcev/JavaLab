package ru.kpfu.itis.rodsher.consumers;

import com.rabbitmq.client.Delivery;
import ru.kpfu.itis.rodsher.services.TemplatePdfCreator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

public class PassportImageConsumer extends QueueConsumer {
    private static final String PASSPORT_IMAGE_QUEUE = "passport_image_queue";

    private final String DOWNLOAD_PATH;

    private TemplatePdfCreator pdfCreator;

    public PassportImageConsumer(String downloadsPath) {
        super(PASSPORT_IMAGE_QUEUE);
        DOWNLOAD_PATH = downloadsPath;
        pdfCreator = new TemplatePdfCreator();
    }

    public static void main(String[] args) {
        PassportImageConsumer passportImageConsumer = new PassportImageConsumer("downloads");
        passportImageConsumer.start();
    }

    @Override
    public void consume(String consumerTag, Delivery message) {
        try {
            String contentType = message.getProperties().getContentType();
            String charset = contentType != null && contentType.contains("CP1251") ? "CP1251" : "UTF-8";
            String[] strings = new String(message.getBody(), charset).split(" ");
            URL[] urls = new URL[strings.length - 1];
            for(int i = 0; i < urls.length; i++) {
                urls[i] = new URL(strings[i]);
            }
            String id = strings[strings.length - 1];

            BufferedImage imageMain = ImageIO.read(urls[0]);
            BufferedImage imageAdd = ImageIO.read(urls[1]);
            String mainExt = urls[0].getFile().substring(urls[0].getFile().lastIndexOf(".") + 1);
            String addExt = urls[1].getFile().substring(urls[1].getFile().lastIndexOf(".") + 1);
            File main = new File( (!DOWNLOAD_PATH.equals("") ? DOWNLOAD_PATH + "/" : "") + "PASSPORT_SCAN_MAIN_" + UUID.randomUUID().toString() + "." + mainExt);
            File add = new File( (!DOWNLOAD_PATH.equals("") ? DOWNLOAD_PATH + "/" : "") + "PASSPORT_SCAN_ADD_" + UUID.randomUUID().toString() + "." + addExt);
            ImageIO.write(imageMain, mainExt, main);
            ImageIO.write(imageAdd, addExt, add);
            if(pdfCreator.createPassportFromImage(main, add, id, null)) {
                System.out.println("Created PASSPORT_IMAGE for " + id);
            } else {
                System.out.println("Failed to create PASSPORT_IMAGE for " + id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
