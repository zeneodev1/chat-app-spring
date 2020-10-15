package com.zeneo.chatappspring.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.zeneo.chatappspring.model.ChangePasswordRequest;
import com.zeneo.chatappspring.model.DB.Invitation;
import com.zeneo.chatappspring.model.DB.Person;
import com.zeneo.chatappspring.model.DB.User;
import com.zeneo.chatappspring.model.UserUpdateRequest;
import com.zeneo.chatappspring.services.ImagesService;
import com.zeneo.chatappspring.services.InvitationService;
import com.zeneo.chatappspring.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private ImagesService imagesService;

    @Autowired
    private InvitationService invitationService;

    @PostMapping("/user/login")
    public Map<String, Object> handleLogin(@RequestBody User user, HttpServletResponse servletResponse) {

        log.info("Sign in in progress");
        if (user == null) {
            throw new RuntimeException("Bad login request");
        }
        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), new ArrayList<>());
        Authentication authentication = authenticationManager.authenticate(token);
        user = userService.getUserByEmail(user.getEmail());
        String JWT_token = JWT.create()
                .withSubject(authentication.getName())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + 10000000))
                .withClaim("uid", user.getId())
                .sign(Algorithm.HMAC256("dklfajgaiodhaodjfahsjauwiqwjeadnaefewjhikdhuiakh"));
        servletResponse.addHeader("X-Authorization", JWT_token);
        servletResponse.addHeader("Access-Control-Expose-Headers", "X-Authorization");
        Cookie cookie = new Cookie("X-Authorization", JWT_token);
        cookie.setMaxAge(10000);
        cookie.setPath("/");
        servletResponse.addCookie(cookie);
        return user.hidePassword();
    }

    @PostMapping("/user/register")
    public User handelRegister(@RequestBody User user) {
        user = userService.registerUser(user);
        return user;
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("authentication.name == #id")
    public User handelGetUser(@PathVariable("id") String id) {
        return userService.getUserData(id);
    }

    @GetMapping("/user/{id}/friends")
    @PreAuthorize("authentication.name == #id")
    public List<Person> handelGetUserFriend(@PathVariable("id") String id) {
        return userService.getUserData(id).getFriends();
    }


    @PostMapping("/user/changePassword")
    @PreAuthorize("authentication.name == #changePasswordRequest.id")
    public void handleChangePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(changePasswordRequest);
    }

    @PostMapping("/user/{id}/picture")
    public void updateUserPhoto(@RequestParam("image") MultipartFile image, @PathVariable("id") String id) {
        String filename = imagesService.store(image, id);
        userService.updateUserPhoto(filename, id);
    }

    @PostMapping("/user/{id}")
    public UserUpdateRequest updateUserData(@RequestBody UserUpdateRequest updateRequest, @PathVariable("id") String id) {
        userService.updateUserData(updateRequest);
        return updateRequest;
    }



    @GetMapping("/user/search")
    public List<User> searchForUsers(@RequestParam("email") String email) {
        return userService.searchForUsersByEmail(email);
    }

    @GetMapping("/images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = imagesService.loadAsResource(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "application/png").body(file);
    }

}
