package com.zeneo.chatappspring.services;

import com.zeneo.chatappspring.model.DB.User;
import com.zeneo.chatappspring.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmailEquals(s);
        log.info(String.valueOf(user));
        if (user == null) {
            throw new RuntimeException("User doesn't exist");
        }
        return new org.springframework.security.core.userdetails.User
                (user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}
