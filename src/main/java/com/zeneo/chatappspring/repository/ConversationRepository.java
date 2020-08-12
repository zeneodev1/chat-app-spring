package com.zeneo.chatappspring.repository;

import com.zeneo.chatappspring.model.Conversation;
import com.zeneo.chatappspring.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@RedisHash
public interface ConversationRepository extends MongoRepository<Conversation, String> {

    List<Conversation> findAllByParticipantsContains(List<User> participants);

}
