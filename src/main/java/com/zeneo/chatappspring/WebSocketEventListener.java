package com.zeneo.chatappspring;

import com.zeneo.chatappspring.model.DB.User;
import com.zeneo.chatappspring.services.UserStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


@Component
public class WebSocketEventListener {

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private UserStatusService userStatusService;

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        int cons = userStatusService.getUserActiveSessions(event.getUser().getName());
        if (cons == 0) {
            Query query = new Query(Criteria.where("id").is(event.getUser().getName()));
            Update update = Update.update("status", "Online");
            mongoOperations.findAndModify(query, update, User.class);
        }
        userStatusService.setUserActiveSessions(cons+1, event.getUser().getName());
        System.out.println(cons + 1);
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        int cons = userStatusService.getUserActiveSessions(event.getUser().getName());
        if (cons == 1) {
            Query query = new Query(Criteria.where("id").is(event.getUser().getName()));
            Update update = Update.update("status", "Offline");
            mongoOperations.findAndModify(query, update, User.class);
        }
        userStatusService.setUserActiveSessions(cons - 1, event.getUser().getName());
        System.out.println(cons - 1);
    }

}
