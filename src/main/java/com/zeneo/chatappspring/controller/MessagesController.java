package com.zeneo.chatappspring.controller;

import com.zeneo.chatappspring.model.DB.Message;
import com.zeneo.chatappspring.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
public class MessagesController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;



    @GetMapping("/messages/{conversationId}")
    private List<Message> getConversationMessages(@PathVariable("conversationId") String conversationId) {
        return messageService.getAllConversationMessages(conversationId);
    }

    @MessageMapping("/messages/{conversationId}")
    private void sendMessage(Message message, @DestinationVariable("conversationId") String conversationId) {
        messageService.addMessage(message);
        simpMessagingTemplate.convertAndSend("/topic/messages/" + conversationId, message);
    }


}
