package ru.kpfu.itis.rodsher.dto;

import java.util.HashMap;
import java.util.Map;

public class WebDto implements Dto {
    private Map<String, Object> map;

    public WebDto(Status status) {
        map = new HashMap<>();
        map.put("#status", status);
    }

    public WebDto(Status status, String key, Object value) {
        map = new HashMap<>();
        map.put("#status", status);
        map.put(key, value);
    }

    @Override
    public Status getStatus() {
        return (Status) map.get("#status");
    }

    @Override
    public void setStatus(Status status) {
        map.put("#status", status);
    }

    @Override
    public void add(String key, Object value) {
        map.put(key, value);
    }

    @Override
    public Object get(String key) {
        return map.get(key);
    }

    @Override
    public Map getMap() {
        return map;
    }
}