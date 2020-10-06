package com.zeneo.chatappspring.repository;

import com.zeneo.chatappspring.model.DB.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByConversationId(String Message);
}
