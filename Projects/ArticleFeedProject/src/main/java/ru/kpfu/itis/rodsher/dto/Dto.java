package ru.kpfu.itis.rodsher.dto;

import java.util.Map;

public interface Dto {
    Status getStatus();
    void setStatus(Status status);
    void add(String key, Object value);
    Object get(String key);
    Map getMap();
}
