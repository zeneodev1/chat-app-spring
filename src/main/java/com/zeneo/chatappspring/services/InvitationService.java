package com.zeneo.chatappspring.services;

import com.zeneo.chatappspring.model.DB.Conversation;
import com.zeneo.chatappspring.model.DB.Invitation;
import com.zeneo.chatappspring.model.DB.Person;
import com.zeneo.chatappspring.model.DB.User;
import com.zeneo.chatappspring.repository.ConversationRepository;
import com.zeneo.chatappspring.repository.InvitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class InvitationService {

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private ConversationRepository conversationRepository;

    public List<Invitation> getPendingToInvitations(String id) {
        return invitationRepository.getByToIdAndStatus(id, "PENDING");
    }

    public List<Invitation> getPendingFromInvitations(String id) {
        return invitationRepository.getByFromIdAndStatus(id, "PENDING");
    }


    public Invitation sendInvitation(String from, String to) {
        Invitation invitation = new Invitation();
        invitation.setFrom(new Person(from));
        invitation.setTo(new Person(to));
        invitation.setFromId(from);
        invitation.setToId(to);
        invitation.setStatus("PENDING");
        return invitationRepository.save(invitation);
    }


    public Invitation acceptInvitation(String fromId, String toId) {
        Update update = new Update();
        update.set("status", "ACCEPTED");
        Query query = new Query(where("fromId").is(fromId).and("toId").is(toId));
        Invitation invitation = mongoOperations.findAndModify(query, update, Invitation.class);


        User u1 = new User();
        assert invitation != null;
        u1.setId(invitation.getFromId());
        User u2 = new User();
        u2.setId(invitation.getToId());
        Update update1 = new Update().push("friends", u1);
        Update update2 = new Update().push("friends", u2);

        query = new Query(where("id").is(invitation.getFromId()));
        mongoOperations.findAndModify(query, update2, User.class);

        query = new Query(where("id").is(invitation.getToId()));
        mongoOperations.findAndModify(query, update1, User.class);

        Conversation conversation = new Conversation();
        conversation.setParticipantsId(Arrays.asList(u1.getId(), u2.getId()));
        conversation.setParticipants(Arrays.asList(new Person(u1.getId()), new Person(u2.getId())));
        conversation.setType("FRIENDS");
        conversation.setLastActivity(new Date(System.currentTimeMillis()));
        Map<String, String> lSM = new HashMap<>();
        lSM.put(u1.getId(), null);
        lSM.put(u2.getId(), null);
        conversation.setLastSeenMessage(lSM);
        Map<String, Integer> unseenMessages = new HashMap<>();
        unseenMessages.put(u1.getId(), 0);
        unseenMessages.put(u2.getId(), 0);
        conversation.setUnseenMessages(unseenMessages);
        conversationRepository.save(conversation).subscribe();
        return invitation;
    }

    public List<Invitation> getAllUserInvitations(String userId) {
        return invitationRepository.getAllByFromIdOrToId(userId, userId);
    }

    public List<Invitation> getImportantInvitations(String userId) {
        Query query = new Query();
        Criteria criteria = new Criteria()
                .orOperator(where("fromId").is(userId).and("status").is("ACCEPTED"),
                        where("toId").is(userId).and("status").is("PENDING"));
        query.addCriteria(criteria);
        return mongoOperations.find(query, Invitation.class);
    }

}
