package com.nva.server.services;

import com.nva.server.dtos.ConversationResponse;
import com.nva.server.entities.Conversation;
import com.nva.server.entities.User;

import java.util.List;
import java.util.Map;

public interface ConversationService {
    Conversation saveConversation(Conversation conversation);
    Conversation editConversation(Conversation Conversation);
    void removeConversation(Conversation Conversation);
    List<Conversation> getConversation(Map<String, Object> params);
    ConversationResponse getConversationsByUser(Map<String, String> params);
    long getConversationCount(Map<String, Object> params);
}
