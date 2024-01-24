package com.nva.server.controllers;

import com.nva.server.services.DialogflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/dialogflow")
public class DialogflowController {

    @Autowired
    private DialogflowService dialogflowService;

    @PostMapping("/query")
    public ResponseEntity<String> handleDialogflowQuery(@RequestBody Map<String, Object> requestBody) {
        String projectId = "automatic-consulting-ekrp";
        String sessionId = "unique-session-id"; // Use a unique session ID for each user
        String userQuery = (String) requestBody.get("query");
        String response = dialogflowService.detectIntent(projectId, sessionId, userQuery);
        return ResponseEntity.ok(response);
    }
}
