package com.zeneo.chatappspring.model;

import lombok.Data;

@Data
public class ChangePasswordRequest {

    private String id;
    private String oldPassword;
    private String newPassword;

}
