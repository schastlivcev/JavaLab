package ru.kpfu.itis.rodsher.consumers;

import com.rabbitmq.client.Delivery;

public class FilesStatisticsConsumer extends ExchangeQueueConsumer {
    private static final String RECEIPT_TOPIC_EXCHANGE = "receipt_topic_exchange";
    private static final String RECEIPT_ROUTING_KEY = "files.*.*";

    private static final String RECEIPT_IMAGE_REGEX = "files\\.image\\..*";
    private static final String RECEIPT_DOC_REGEX = "files\\.document\\..*";

    private int imagesNum;
    private int docsNum;

    public FilesStatisticsConsumer() {
        super(RECEIPT_TOPIC_EXCHANGE, RECEIPT_ROUTING_KEY);
        imagesNum = 0;
        docsNum = 0;
    }

    public static void main(String[] args) {
        FilesStatisticsConsumer filesStatisticsConsumer = new FilesStatisticsConsumer();
        filesStatisticsConsumer.start();
    }

    @Override
    public void consume(String consumerTag, Delivery message) {
        String id = new String(message.getBody()).split(" ")[1];
        if(message.getEnvelope().getRoutingKey().matches(RECEIPT_IMAGE_REGEX)) {
            imagesNum++;
        } else if(message.getEnvelope().getRoutingKey().matches(RECEIPT_DOC_REGEX)) {
            docsNum++;
        }
        System.out.println("Images = " + imagesNum + ", Docs = " + docsNum + ", Total = " + getFilesNum() + ", last RECEIPT id = " + id);
    }

    private int getFilesNum() {
        return imagesNum + docsNum;
    }
}
