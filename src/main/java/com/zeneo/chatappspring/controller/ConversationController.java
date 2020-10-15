package com.zeneo.chatappspring.controller;

import com.zeneo.chatappspring.model.ChangeEvent;
import com.zeneo.chatappspring.model.DB.Conversation;
import com.zeneo.chatappspring.services.ConversationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@Slf4j
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @GetMapping("/conversations/{userId}")
    public Flux<Conversation> getUserConversations(@PathVariable("userId") String id) {
        return conversationService.getAllUserConversations(id);
    }

    @GetMapping("/live/conversations/{userId}")
    public Flux<ChangeEvent<Conversation>> getUserConversationsListener(@PathVariable("userId") String id) {
        return conversationService.listenToAllUserConversations(id).map(conversationChangeStreamEvent -> {
            ChangeEvent<Conversation> changeEvent = new ChangeEvent<>();
            switch (conversationChangeStreamEvent.getRaw().getOperationType()) {
                case INSERT:
                    changeEvent.setEventType("insert");
                    changeEvent.setDocument(conversationChangeStreamEvent.getBody());
                    break;
                case UPDATE:
                    changeEvent.setEventType("update");
                    changeEvent.setDocument(conversationChangeStreamEvent.getBody());
                    break;
                case DELETE:
                    changeEvent.setEventType("delete");
                    Conversation conversation = new Conversation();
                    conversation.setId(conversationChangeStreamEvent.getRaw().getDocumentKey().getFirstKey());
                    changeEvent.setDocument(conversation);
                    break;
            }

            return changeEvent;
        });
    }

    @GetMapping("/conversation/{conId}")
    public Mono<Conversation> getConversation(@PathVariable("conId") String id) {
        return conversationService.getConversationById(id);
    }

    @PutMapping("/conversations/{userId}")
    public void updateConversations(@RequestBody Conversation conversation, @PathVariable("userId") String id) {
        conversationService.updateConversation(conversation);
    }

}
