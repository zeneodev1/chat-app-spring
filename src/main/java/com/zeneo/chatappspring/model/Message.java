package com.zeneo.chatappspring.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@Document
public class Message {

    @Id
    private String id;
    private String text;
    private Date sentAt;
    private String status;
    private String from;
    private String conId;
    private List<MessageStatus> messageStatuses;
}
