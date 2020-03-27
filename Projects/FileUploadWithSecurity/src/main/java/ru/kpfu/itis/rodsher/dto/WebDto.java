package ru.kpfu.itis.rodsher.dto;

import java.util.HashMap;
import java.util.Map;

public class WebDto implements Dto {
    private Status status;
    private Map<String, Object> payload;

    public WebDto(Status status) {
        this.status = status;
        payload = new HashMap<>();
    }

    public WebDto(Status status, Map<String, Object> payload) {
        this.status = status;
        this.payload = payload;
    }

    public WebDto(Status status, String key, Object value) {
        this.status = status;
        payload = new HashMap<>();
        payload.put(key, value);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }

    public void addToPayload(String key, Object value) {
        payload.put(key, value);
    }

    public Object getFromPayload(String key) {
        return payload.get(key);
    }
}
