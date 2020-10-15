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

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private UserStatusService userStatusService;

    public List<Message> getAllConversationMessages(String conversationId) {
        return messageRepository.findByConversationId(conversationId);
    }

    public void addMessage(Message message) {
        message.setSentAt(new Date(System.currentTimeMillis()));
        message = messageRepository.save(message);
        List<String> participantsId = userStatusService.getConversationParticipantIds(message.getConversationId());
        if (participantsId != null) {
            updateConversation(message, participantsId);
        } else {
            Conversation conversation = mongoOperations.findOne(new Query(where("id").is(message.getConversationId())), Conversation.class);
            assert conversation != null;
            participantsId = conversation.getParticipantsId();
            userStatusService.setConversationParticipantIds(participantsId, conversation.getId());
            updateConversation(message, participantsId);
        }

    }

    public Conversation updateConversation(Message message, List<String> ids) {
        Query q = new Query(where("id").is(message.getConversationId()));
        Update u = new Update();
        u.set("lastMessage", message);
        u.set("lastActivity", new Date(System.currentTimeMillis()));
        ids.forEach(s -> {
            if (!s.equals(message.getFrom()))
                u.inc("unseenMessages." + s, 1);
        });
        u.set("lastSeenMessage." + message.getFrom(), message.getId());
        return mongoOperations.findAndModify(q, u, Conversation.class);
    }
}
