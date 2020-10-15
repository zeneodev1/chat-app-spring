package com.zeneo.chatappspring.controller;

import com.zeneo.chatappspring.model.DB.Invitation;
import com.zeneo.chatappspring.services.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InvitationController {

    @Autowired
    private InvitationService invitationService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @GetMapping("/invitations/all/{userId}")
    public List<Invitation> getAllInvitations(@PathVariable("userId") String userId) {
        return invitationService.getAllUserInvitations(userId);
    }

    @GetMapping("/invitations/important/{userId}")
    public List<Invitation> getImportantInvitations(@PathVariable("userId") String userId) {
        return invitationService.getImportantInvitations(userId);
    }

    @GetMapping("/invitations/from/{userId}")
    public List<Invitation> getAllFromInvitations(@PathVariable String userId) {
        return invitationService.getPendingFromInvitations(userId);
    }

    @GetMapping("/invitations/to/{userId}")
    public List<Invitation> getAllToInvitations(@PathVariable String userId) {
        return invitationService.getPendingToInvitations(userId);
    }

    @PostMapping("/invitations/{from}/{to}")
    public Invitation handleSendingInvitation(@PathVariable String from, @PathVariable String to) {
        Invitation invitation = invitationService.sendInvitation(from, to);
        messagingTemplate.convertAndSend("/invitations/" + to, invitation);
        return invitation;
    }

    @PostMapping("/invitations/accept/{from}/{to}")
    private void handleAcceptInvitation(@PathVariable("to") String toId, @PathVariable("from") String fromId) {
        Invitation invitation = invitationService.acceptInvitation(fromId, toId);
        messagingTemplate.convertAndSend("/invitations/" + fromId, invitation);
    }

}
