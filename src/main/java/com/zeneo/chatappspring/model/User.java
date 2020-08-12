package com.zeneo.chatappspring.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@Document
public class User implements Serializable {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String photo;
    private String status;
    @DBRef(lazy = true)
    @ToString.Exclude
    private List<User> friends = new ArrayList<>();
    private UserProfile profile;

}
