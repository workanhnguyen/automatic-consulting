package com.nva.server.services;

import com.nva.server.dtos.CustomDialogflowResponse;

public interface DialogflowService {
    CustomDialogflowResponse detectIntent(String projectId, String sessionId, String text);
}
