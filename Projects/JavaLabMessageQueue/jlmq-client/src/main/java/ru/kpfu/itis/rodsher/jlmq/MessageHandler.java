package ru.kpfu.itis.rodsher.jlmq;

@FunctionalInterface
public interface MessageHandler {
    void handleMessage(MessageDto message);
}
