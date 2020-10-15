package com.zeneo.chatappspring.model.DB;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@ToString
@Document
public class Conversation {

    @Id
    private String id;
    private String type;
    @DBRef
    private List<Person> participants;
    private List<String> participantsId;
    private Message lastMessage;
    private Date lastActivity;
    private Map<String, Integer> unseenMessages;
    private Map<String, String> lastSeenMessage;
}
