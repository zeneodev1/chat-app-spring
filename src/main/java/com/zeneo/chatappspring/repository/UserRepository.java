package com.zeneo.chatappspring.repository;

import com.zeneo.chatappspring.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
