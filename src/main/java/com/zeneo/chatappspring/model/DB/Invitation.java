package com.zeneo.chatappspring.model.DB;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document
public class Invitation {

    @Id
    private String id;
    private String status;
    @DBRef
    private Person from;
    @DBRef
    private Person to;
    private String fromId;
    private String toId;

}
