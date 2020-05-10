package ru.kpfu.itis.rodsher.jlmq.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.rodsher.jlmq.dto.Dto;
import ru.kpfu.itis.rodsher.jlmq.dto.Status;
import ru.kpfu.itis.rodsher.jlmq.dto.WebDto;
import ru.kpfu.itis.rodsher.jlmq.models.Queue;
import ru.kpfu.itis.rodsher.jlmq.repositories.QueuesRepository;

@Service
public class QueueServiceImpl implements QueueService {
    @Autowired
    private QueuesRepository queuesRepository;

    @Override
    public Dto createQueue(String name) {
        if(queuesRepository.findByName(name).isPresent()) {
            return new WebDto(Status.QUEUE_CREATE_ERROR);
        }
        queuesRepository.save(Queue.builder().name(name).build());
        return new WebDto(Status.QUEUE_CREATE_SUCCESS);
    }
}