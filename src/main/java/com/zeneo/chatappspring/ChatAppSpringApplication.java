package com.zeneo.chatappspring;

import com.zeneo.chatappspring.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.ObjectHashMapper;

import java.util.Date;
import java.util.Map;

@SpringBootApplication
public class ChatAppSpringApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(ChatAppSpringApplication.class, args);
    }


    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    HashMapper<Object, byte[], byte[]> mapper = new ObjectHashMapper();

    public void writeHash(String key, UserProfile userProfile) {

        HashOperations<String, byte[], byte[]> hashOperations = redisTemplate.opsForHash();
        Map<byte[], byte[]> mappedHash = mapper.toHash(userProfile);
        hashOperations.putAll(key, mappedHash);
    }

    public UserProfile loadHash(String key) {
        HashOperations<String, byte[], byte[]> hashOperations = redisTemplate.opsForHash();
        Map<byte[], byte[]> loadedHash = hashOperations.entries(key);
        return (UserProfile) mapper.fromHash(loadedHash);
    }

    @Override
    public void run(ApplicationArguments args) {
        UserProfile userProfile = new UserProfile();
        userProfile.setGender("Male");
        userProfile.setPhone("+212698563902");
        userProfile.setBirthDay(new Date(System.currentTimeMillis()));
        writeHash("1", userProfile);
        System.out.println(loadHash("1"));
    }

}
