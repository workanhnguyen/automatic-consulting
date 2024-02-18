package com.nva.server.apis;

import com.nva.server.dtos.CustomDialogflowResponse;
import com.nva.server.services.DialogflowService;
import com.nva.server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/dialogflow")
public class DialogflowApi {
    @Autowired
    private Environment env;
    @Autowired
    private DialogflowService dialogflowService;
    @Autowired
    private UserService userService;

    @PostMapping("/query")
    public ResponseEntity<CustomDialogflowResponse> handleDialogflowQuery(@RequestBody Map<String, Object> requestBody) {
        String projectId = env.getProperty("dialogflow.project-name");
        String uniqueSessionId = SecurityContextHolder.getContext().getAuthentication().getName();
        String userQuery = (String) requestBody.get("query");

        return ResponseEntity.ok(dialogflowService.detectIntent(projectId, uniqueSessionId, userQuery));
    }
}
