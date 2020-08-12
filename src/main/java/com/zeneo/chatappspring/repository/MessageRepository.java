package com.zeneo.chatappspring.repository;

import com.zeneo.chatappspring.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends MongoRepository<Message, String> {
}
