package com.zeneo.chatappspring.services;

import com.zeneo.chatappspring.model.ChangePasswordRequest;
import com.zeneo.chatappspring.model.DB.User;
import com.zeneo.chatappspring.model.UserUpdateRequest;
import com.zeneo.chatappspring.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MongoTemplate mongoTemplate;

    public User getUserData(String id) {
        User user = userRepository.findById(id).orElseThrow(RuntimeException::new);
        return user;
    }

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    public List<User> searchForUsersByEmail(String email) {
        return userRepository.findAllByEmailContains(email, PageRequest.of(0, 10));
    }


    public void changePassword(ChangePasswordRequest passwordRequest) {
        User user = userRepository.findById(passwordRequest.getId()).orElseThrow(RuntimeException::new);
        if (!passwordEncoder.matches(passwordRequest.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Password wrong");
        }
        user.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
        userRepository.save(user);

    }

    public User updateUser(User user) {
        User old = userRepository.findById(user.getId()).orElseThrow(RuntimeException::new);
        user.setPassword(old.getPassword());
        userRepository.save(user);
        return old;
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmailEquals(email);
    }

    public void updateUserPhoto(String fileName, String userId) {
        mongoTemplate.findAndModify(new Query(where("id").is(userId)),
                Update.update("photo", fileName),
                User.class);
    }

    public User updateUserData(UserUpdateRequest updateRequest) {
        Update update = new Update();
        update.set("firstName", updateRequest.getFirstName());
        update.set("lastName", updateRequest.getLastName());
        update.set("email", updateRequest.getEmail());
        update.set("location", updateRequest.getLocation());
        return mongoTemplate.findAndModify(new Query(where("id").is(updateRequest.getId())),
                update,
                User.class);
    }
}
