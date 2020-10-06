package com.zeneo.chatappspring.model;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String location;
}
