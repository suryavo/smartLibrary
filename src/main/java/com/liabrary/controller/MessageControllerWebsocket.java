package com.liabrary.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.liabrary.dao.UserRepository;
import com.liabrary.entities.ChatMessage;
import com.liabrary.entities.User;

@RestController
@CrossOrigin
public class MessageControllerWebsocket {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private UserRepository userRepository;

    @MessageMapping("/chat/{to}")
    public void sendMessage(@DestinationVariable int to, ChatMessage message) {
        System.out.println("handling send message: " + message + " to: " + to);
        User user=this.userRepository.getById(to);
        if (user!=null) {
            simpMessagingTemplate.convertAndSend("/topic/messages/" + to, message);
        }
    }
}