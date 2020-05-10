package ru.kpfu.itis.rodsher.jlmq.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kpfu.itis.rodsher.jlmq.dto.Status;
import ru.kpfu.itis.rodsher.jlmq.services.QueueService;

@Controller
public class QueueController {
    @Autowired
    private QueueService queueService;

    @GetMapping("/create")
    public String getCreatePage() {
        return "create";
    }

    @PostMapping("/create")
    public String createQueue(@RequestParam("name") String queueName, ModelMap map) {
        if(queueService.createQueue(queueName).getStatus().equals(Status.QUEUE_CREATE_SUCCESS)) {
            return "redirect:/";
        }
        map.put("name", queueName);
        map.put("error", "Already exists");
        return "create";
    }

    @GetMapping("/list")
    public String getMessagesList() {
        return "list";
    }
}
