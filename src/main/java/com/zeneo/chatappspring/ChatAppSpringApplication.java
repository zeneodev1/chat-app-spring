package com.zeneo.chatappspring;

import com.zeneo.chatappspring.model.DB.Conversation;
import com.zeneo.chatappspring.model.DB.User;
import com.zeneo.chatappspring.repository.ConversationRepository;
import com.zeneo.chatappspring.repository.UserRepository;
import com.zeneo.chatappspring.services.ImagesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@SpringBootApplication
@Slf4j
public class ChatAppSpringApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(ChatAppSpringApplication.class, args);
    }


    @Autowired
    UserRepository userRepository;
    @Autowired
    ConversationRepository conversationRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ReactiveMongoTemplate reactiveMongoTemplate;

    @Autowired
    ImagesService imagesService;

    @Override
    public void run(ApplicationArguments args) {
        imagesService.init();
    }



}
