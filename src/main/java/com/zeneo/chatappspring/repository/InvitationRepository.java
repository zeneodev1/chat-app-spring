package com.zeneo.chatappspring.repository;

import com.zeneo.chatappspring.model.DB.Invitation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InvitationRepository extends MongoRepository<Invitation, String> {

    List<Invitation> getByToIdAndStatus(String toId, String status);
    List<Invitation> getByFromIdAndStatus(String toId, String status);
    List<Invitation> getAllByFromIdOrToId(String fromId, String toId);
}
