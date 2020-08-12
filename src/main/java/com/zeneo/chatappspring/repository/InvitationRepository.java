package com.zeneo.chatappspring.repository;

import com.zeneo.chatappspring.model.Invitation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InvitationRepository extends MongoRepository<Invitation, String> {
}
