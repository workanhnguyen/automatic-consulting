package com.nva.server.services;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.*;
import com.google.protobuf.Value;
import com.nva.server.dtos.CustomDialogflowResponse;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class DialogflowService {
    private final SessionsClient sessionsClient;
    private final String credentialsPath = "src/main/resources/dialogflow-credentials-path.json";
    private final String googleCloudPath = "https://www.googleapis.com/auth/cloud-platform";

    public DialogflowService() throws IOException {
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

        CustomDialogflowResponse response = new CustomDialogflowResponse();
        response.setIntentDisplayName(intentResponse.getQueryResult().getIntent().getDisplayName());
        response.setFulfillmentText(intentResponse.getQueryResult().getFulfillmentText());
        response.setParameters(convertParameters(intentResponse.getQueryResult().getParameters().getFieldsMap()));

        return response;
    }

    private Map<String, Object> convertParameters(Map<String, Value> parameters) {
        Map<String, Object> convertedParameters = new HashMap<>();
        for (Map.Entry<String, Value> entry : parameters.entrySet()) {
            convertedParameters.put(entry.getKey(), entry.getValue().getStringValue());
        }
        return convertedParameters;
    }
}
