package com.zeneo.chatappspring.services;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserStatusService {

    @Cacheable(cacheNames = "user_connections", key = "#userId")
    public int getUserActiveSessions(String userId) {
        return 0;
    }


    @CachePut(cacheNames = "user_connections", key = "#userId")
    public int setUserActiveSessions(int n, String userId) {
        return n;
    }


    @Cacheable(cacheNames = "conversation_participants", key = "#conversationId")
    public List<String> getConversationParticipantIds(String conversationId) {
        return null;
    }

    @CachePut(cacheNames = "conversation_participants", key = "#conversationId")
    public List<String> setConversationParticipantIds(List<String> ids, String conversationId) {
        return ids;
    }

}
