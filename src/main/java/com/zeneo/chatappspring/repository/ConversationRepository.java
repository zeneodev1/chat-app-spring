package com.zeneo.chatappspring.repository;

import com.zeneo.chatappspring.model.DB.Conversation;
import com.zeneo.chatappspring.model.DB.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import java.util.List;


public interface ConversationRepository extends ReactiveMongoRepository<Conversation, String> {

    @Query("{ 'participantsId': { $all: [?0] } }")
    Flux<Conversation> findByParticipantId(String id, Sort sort);

}
