package com.zeneo.chatappspring.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Setter
@Getter
@ToString
@Document
public class Conversation {

    @Id
    private String id;
    private String type;
    @DBRef
    private List<User> participants;
    private Message lastMessage;
}
