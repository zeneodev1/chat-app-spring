package com.zeneo.chatappspring.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
public class UserProfile {
    private String gender;
    private String phone;
    private Date birthDay;
}
