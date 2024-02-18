package com.nva.server.services;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.*;
import com.google.protobuf.ListValue;
import com.google.protobuf.Value;
import com.nva.server.dtos.CustomDialogflowResponse;
import com.nva.server.entities.Conversation;
import com.nva.server.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Service
public class DialogflowService {
    @Autowired
    private UserService userService;
    @Autowired
    private ConversationService conversationService;

    private final SessionsClient sessionsClient;

    public DialogflowService() throws IOException {
        String googleCloudPath = "https://www.googleapis.com/auth/cloud-platform";
        String credentialsPath = "src/main/resources/dialogflow-credentials-path.json";
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsPath))
                .createScoped(Collections.singletonList(googleCloudPath));
        sessionsClient = SessionsClient.create(
                SessionsSettings.newBuilder()
                        .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                        .build());
    }

    public CustomDialogflowResponse detectIntent(String projectId, String sessionId, String text) {
        SessionName session = SessionName.of(projectId, sessionId);
        TextInput.Builder textInput = TextInput.newBuilder().setText(text).setLanguageCode("vi-VN");
        QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();
        DetectIntentResponse intentResponse = sessionsClient.detectIntent(session, queryInput);

        String intent = intentResponse.getQueryResult().getIntent().getDisplayName();
        String fulfillmentText = intentResponse.getQueryResult().getFulfillmentText();
        Map<String, Object> parameters = convertParameters(intentResponse.getQueryResult().getParameters().getFieldsMap());

        // Save conversation to database
        Optional<User> requestUser = userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (requestUser.isPresent()) {
            Conversation conversation = new Conversation();
            conversation.setUser(requestUser.get());
            conversation.setRequestText(text);
            conversation.setResponseText(fulfillmentText);

            conversationService.saveConversation(conversation);
        }

        // Send response to client
        CustomDialogflowResponse response = new CustomDialogflowResponse();
        response.setResponseText(fulfillmentText);
        response.setCreatedDate(new Date().getTime());

        return response;
    }

    private Map<String, Object> convertParameters(Map<String, Value> parameters) {
        Map<String, Object> convertedParameters = new HashMap<>();
        for (Map.Entry<String, Value> entry : parameters.entrySet()) {
            convertedParameters.put(entry.getKey(), convertValue(entry.getValue()));
        }
        return convertedParameters;
    }

    private Object convertValue(Value value) {
        return switch (value.getKindCase()) {
            case STRING_VALUE -> value.getStringValue();
            case LIST_VALUE -> convertList(value.getListValue());
            case STRUCT_VALUE -> convertParameters(value.getStructValue().getFieldsMap());
            default -> null;
        };
    }

    private List<Object> convertList(ListValue listValue) {
        List<Object> list = new ArrayList<>();
        for (Value value : listValue.getValuesList()) {
            list.add(convertValue(value));
        }
        return list;
    }
}
