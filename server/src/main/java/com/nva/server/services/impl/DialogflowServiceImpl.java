package com.nva.server.services.impl;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.*;
import com.google.protobuf.ListValue;
import com.google.protobuf.Value;
import com.nva.server.dtos.CustomDialogflowResponse;
import com.nva.server.entities.Conversation;
import com.nva.server.entities.Information;
import com.nva.server.entities.User;
import com.nva.server.exceptions.CommonException;
import com.nva.server.services.ConversationService;
import com.nva.server.services.DialogflowService;
import com.nva.server.services.InformationService;
import com.nva.server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Service
public class DialogflowServiceImpl implements DialogflowService {
    @Autowired
    private UserService userService;
    @Autowired
    private ConversationService conversationService;
    @Autowired
    private InformationService informationService;

    private final SessionsClient sessionsClient;

    public DialogflowServiceImpl() throws IOException {
        String googleCloudPath = "https://www.googleapis.com/auth/cloud-platform";
        String credentialsPath = "src/main/resources/dialogflow-credentials-path.json";
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsPath))
                .createScoped(Collections.singletonList(googleCloudPath));
        sessionsClient = SessionsClient.create(
                SessionsSettings.newBuilder()
                        .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                        .build());
    }

    @Override
    public CustomDialogflowResponse detectIntent(String projectId, String sessionId, String text) {
        try {
            SessionName session = SessionName.of(projectId, sessionId);
            TextInput.Builder textInput = TextInput.newBuilder().setText(text).setLanguageCode("vi-VN");
            QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();
            DetectIntentResponse intentResponse = sessionsClient.detectIntent(session, queryInput);

            String intent = intentResponse.getQueryResult().getIntent().getDisplayName();
            String answer;
            if (intent.equals("default-fallback") || intent.equals("default-welcome")) {
                answer = intentResponse.getQueryResult().getFulfillmentText();
            } else {
                List<String> intentParts = Arrays.asList(intent.split("\\."));
                Map<String, String> params = new HashMap<>();
                params.put("action", intentParts.get(0));
                params.put("scope", intentParts.get(1));
                params.put("topic", intentParts.get(2));
                List<Information> result = informationService.getInformationByIntent(params);
                answer = result.get(0).getContent();
//        Map<String, Object> parameters = convertParameters(intentResponse.getQueryResult().getParameters().getFieldsMap());
            }

            // Save conversation to database
            Optional<User> requestUser = userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
            if (requestUser.isPresent()) {
                Conversation conversation = new Conversation();
                conversation.setUser(requestUser.get());
                conversation.setRequestText(text);
                conversation.setResponseText(answer);

                conversationService.saveConversation(conversation);

                // Send response to client
                CustomDialogflowResponse response = new CustomDialogflowResponse();
                response.setQuestion(text);
                response.setAnswer(answer);
                response.setCreatedDate(new Date().getTime());

                return response;
            } return null;
        } catch (Exception e) {
            throw new CommonException("Đặt câu hỏi thất bại. Vui lòng thử lại sau");
        }
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
