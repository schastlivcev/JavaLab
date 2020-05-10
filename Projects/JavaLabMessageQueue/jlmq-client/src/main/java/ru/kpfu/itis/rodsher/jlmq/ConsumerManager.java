package ru.kpfu.itis.rodsher.jlmq;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ConsumerManager {
    private final Map<String, Consumer> consumers = new HashMap<>();

    protected void addConsumer(String queue, Consumer consumer) {
        consumers.put(queue, consumer);
    }

    protected Optional<Consumer> getConsumerFor(String queue) {
        return Optional.ofNullable(consumers.get(queue));
    }

    protected void removeConsumer(String queue) {
        consumers.remove(queue);
    }
}
