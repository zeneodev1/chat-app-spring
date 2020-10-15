package com.zeneo.chatappspring.services;

import com.zeneo.chatappspring.model.DB.Conversation;
import com.zeneo.chatappspring.model.LastSeenRequest;
import com.zeneo.chatappspring.repository.ConversationRepository;
import com.zeneo.chatappspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.mongodb.core.query.Criteria.where;


@Service
public class ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Autowired
    private MongoOperations mongoOperations;


    public Flux<ChangeStreamEvent<Conversation>> listenToAllUserConversations(String userId) {
        return reactiveMongoTemplate.changeStream(Conversation.class)
                .watchCollection("conversation")
                .filter(where("participantsId").all(userId))
                .listen();
    }

    public Flux<Conversation> getAllUserConversations(String userId) {
        return conversationRepository.findByParticipantId(userId, Sort.by("lastActivity").descending());
    }

    public Mono<Conversation> getConversationById(String conId) {
        return conversationRepository.findById(conId);
    }

    public void createConversation(Conversation conversation) {
        conversationRepository.save(conversation).subscribe();
    }

    public void updateConversation(Conversation conversation) {
        if (conversation.getId() != null) {
            conversationRepository.save(conversation).subscribe();
        }
    }

    public void deleteConversation(Conversation conversation) {
        conversationRepository.delete(conversation).subscribe();
    }

    public Conversation updateLastSeen(LastSeenRequest request) {
        Query query = new Query(where("id").is(request.getConversationId()));
        Update update = new Update();
        update.set("unseenMessages." + request.getUserId(), 0);
        update.set("lastSeenMessage." + request.getUserId(), request.getMessageId());
        return mongoOperations.findAndModify(query, update, Conversation.class);
    }
}
