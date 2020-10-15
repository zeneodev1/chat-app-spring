package com.zeneo.chatappspring.model.DB;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.*;

@Data
@ToString
@NoArgsConstructor
@Document
public class User implements Serializable {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    @Indexed(unique = true)
    private String email;
    private String password;
    private String photo;
    private String status;
    private Date createdAt;
    @DBRef(lazy = true)
    @JsonIgnore
    @ToString.Exclude
    private List<Person> friends = new ArrayList<>();
    private String gender;
    private String phone;
    private Date birthDay;
    private String location;

    public User(String id) {
        this.id = id;
    }

    public Map<String, Object> hidePassword() {
        Map<String, Object> map = new HashMap<>();
        map.put("firstName", this.getFirstName());
        map.put("lastName", this.getLastName());
        map.put("email", this.getEmail());
        map.put("photo", this.getPhoto());
        map.put("id", this.getId());
        map.put("gender", this.getGender());
        map.put("phone", this.getPhone());
        map.put("birthDay", this.getBirthDay());
        return map;
    }

}
