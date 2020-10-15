package com.zeneo.chatappspring.model.DB;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "user")
@Data
public class Person {

    @Id
    private String id;
    private String firstName;
    private String lastName;

    @Indexed(unique = true)
    private String email;
    private String photo;
    private String status;
    private Date createdAt;
    private String gender;
    private String phone;
    private Date birthDay;
    private String location;

    public Person (String id) {
        this.id = id;
    }

}
