package com.nva.server.apis;

import com.nva.server.dtos.ConversationResponse;
import com.nva.server.services.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/conversations")
@CrossOrigin
public class ConversationApi {
    @Autowired
    private ConversationService conversationService;

    @GetMapping
    public ResponseEntity<ConversationResponse> getConversationsByUser(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(conversationService.getConversationsByUser(params));
    }
}
