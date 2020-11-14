package ru.kpfu.itis.rodsher.consumers;

import com.rabbitmq.client.Delivery;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class FilesDownloadConsumer extends ExchangeQueueConsumer {
    private static final String RECEIPT_TOPIC_EXCHANGE = "receipt_topic_exchange";
    private static final String RECEIPT_ROUTING_KEY = "files.*.*";

    private final String DOWNLOAD_PATH;

    public FilesDownloadConsumer(String downloadsPath) {
        super(RECEIPT_TOPIC_EXCHANGE, RECEIPT_ROUTING_KEY);
        DOWNLOAD_PATH = downloadsPath;
    }

    public static void main(String[] args) {
        FilesDownloadConsumer filesDownloadConsumer = new FilesDownloadConsumer("downloads");
        filesDownloadConsumer.start();
    }

    @Override
    public void consume(String consumerTag, Delivery message) {
        try {
            String[] strings = new String(message.getBody()).split(" ");
            URL url = new URL(strings[0]);
            String fileExt = url.getFile().substring(url.getFile().lastIndexOf(".") + 1);
            String id = strings[1];

            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            File output = new File((!DOWNLOAD_PATH.equals("") ? DOWNLOAD_PATH + "/" : "") + id + "." + fileExt);
            FileOutputStream fileOutputStream = new FileOutputStream(output);
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

            System.out.println("Downloaded RECEIPT for " + id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
