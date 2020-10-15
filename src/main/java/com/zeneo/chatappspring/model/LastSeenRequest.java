package com.zeneo.chatappspring.model;

import lombok.Data;

@Data
public class LastSeenRequest {
    private String conversationId;
    private String messageId;
    private String userId;
}
