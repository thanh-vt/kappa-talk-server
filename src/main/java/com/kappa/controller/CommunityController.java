package com.kappa.controller;

import com.kappa.model.entity.ChatInfo;
import com.kappa.service.ConversationService;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(originPatterns = "*", allowCredentials = "true", allowedHeaders = "*", exposedHeaders = {
    HttpHeaders.SET_COOKIE})
public class CommunityController {

    private final SimpUserRegistry simpUserRegistry;

    private final ConversationService conversationService;

    @Autowired
    public CommunityController(
        SimpUserRegistry simpUserRegistry,
        ConversationService conversationService) {
        this.simpUserRegistry = simpUserRegistry;
        this.conversationService = conversationService;
    }

    @GetMapping("/users/online")
    public ResponseEntity<Map<String, Boolean>> getOnlineUser() {
        Map<String, Boolean> onlineUserMap = this.simpUserRegistry.getUsers()
            .stream()
            .collect(Collectors.toMap(SimpUser::getName, value -> true));
        return ResponseEntity.ok(onlineUserMap);
    }

    @GetMapping("/chat-info")
    public ResponseEntity<ChatInfo> getChatInfo(@RequestParam("from") String fromUser,
        @RequestParam("to") String toUser) throws InterruptedException {
        ChatInfo chatInfo = this.conversationService.getChatInfo(fromUser, toUser);
        return ResponseEntity.ok(chatInfo);
    }

}
