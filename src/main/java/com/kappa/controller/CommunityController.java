package com.kappa.controller;

import com.kappa.model.entity.Conversation;
import com.kappa.model.entity.Message;
import com.kappa.service.ConversationService;
import com.kappa.service.MessageService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
  * @created 25/04/2021 - 12:58:57 SA
  * @project vengeance
  * @author thanhvt
  * @description
  * @since 1.0
**/
@RestController
@RequestMapping("/community")
@CrossOrigin(originPatterns = "*", allowCredentials = "true", allowedHeaders = "*",
    exposedHeaders = {HttpHeaders.SET_COOKIE})
public class CommunityController {

    private final ConversationService conversationService;

    private final MessageService messageService;

    @Autowired
    public CommunityController(
        ConversationService conversationService, MessageService messageService) {
        this.conversationService = conversationService;
        this.messageService = messageService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/conversation-list")
    public ResponseEntity<Map<String, Conversation>> getConversations(Pageable pageable) {
        Map<String, Conversation> onlineUserMap = this.conversationService.getConversations(pageable);
        return ResponseEntity.ok(onlineUserMap);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/online-users")
    public ResponseEntity<List<String>> getOnlineUsers() {
        List<String> onlineUserMap = this.conversationService.getOnlineUsers();
        return ResponseEntity.ok(onlineUserMap);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/conversation-by-id")
    public ResponseEntity<Conversation> getConversation(@RequestParam("id") String id,
        @RequestParam(value = "fetch-message", defaultValue = "false") boolean isFetchMessage,
        @RequestParam(value = "update-status", defaultValue = "true") boolean updateStatus) {
        Conversation chatInfo = this.conversationService.getConversation(id, isFetchMessage, updateStatus);
        return ResponseEntity.ok(chatInfo);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/conversation")
    public ResponseEntity<Conversation> getConversation(@RequestParam("from") String fromUser,
        @RequestParam(value = "to", required = false) String toUser,
        @RequestParam(value = "fetch-message", defaultValue = "false") boolean isFetchMessage,
        @RequestParam(value = "update-status", defaultValue = "true") boolean updateStatus) {
        Conversation chatInfo = this.conversationService.getConversation(fromUser, toUser, isFetchMessage, updateStatus);
        return ResponseEntity.ok(chatInfo);
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/message")
    public ResponseEntity<Void> updateMessage(@RequestBody Message message) {
        this.messageService.updateMessage(message);
        return ResponseEntity.ok().build();
    }

}
