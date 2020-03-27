package ru.kpfu.itis.rodsher.dto;

import java.util.Map;

public interface Dto {
    Status getStatus();
    void setStatus(Status status);
    Map<String, Object> getPayload();
    void setPayload(Map<String, Object> payload);
    Object getFromPayload(String key);
    void addToPayload(String key, Object value);
}
