package ru.kpfu.itis.rodsher.jlmq;

public class Jlmq {

    public static StompConnector.UriBuilder connector() {
        return StompConnector.builder();
    }
}
