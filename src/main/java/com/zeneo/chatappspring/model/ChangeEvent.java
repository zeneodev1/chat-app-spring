package com.zeneo.chatappspring.model;

import lombok.Data;

@Data
public class ChangeEvent<T> {
    private T document;
    private String eventType;
}
