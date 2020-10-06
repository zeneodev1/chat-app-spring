package com.zeneo.chatappspring.services;

import com.zeneo.chatappspring.model.DB.Conversation;
import com.zeneo.chatappspring.model.DB.Message;
import com.zeneo.chatappspring.repository.ConversationRepository;
import com.zeneo.chatappspring.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MongoOperations mongoOperations;

    public List<Message> getAllConversationMessages(String conversationId) {
        return messageRepository.findByConversationId(conversationId);
    }

    public void addMessage(Message message) {
        message.setSentAt(new Date(System.currentTimeMillis()));
        messageRepository.save(message);
        Query q = new Query(Criteria.where("_id").is(message.getConversationId()));
        Update u = new Update();
        u.set("lastMessage", message);
        u.set("lastActivity", new Date(System.currentTimeMillis()));
        mongoOperations.findAndModify(q, u, Conversation.class);
    }
}
