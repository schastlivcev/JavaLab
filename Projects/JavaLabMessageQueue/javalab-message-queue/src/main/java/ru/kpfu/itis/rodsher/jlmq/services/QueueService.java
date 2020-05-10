package ru.kpfu.itis.rodsher.jlmq.services;

import ru.kpfu.itis.rodsher.jlmq.dto.Dto;

public interface QueueService {
    Dto createQueue(String name);
}
