package com.nva.server.services;

import com.nva.server.entities.SuggestedQuestion;

import java.util.List;
import java.util.Map;

public interface SuggestedQuestionService {
    SuggestedQuestion saveSuggestedQuestion(SuggestedQuestion scope);
    SuggestedQuestion editSuggestedQuestion(SuggestedQuestion scope);
    void removeSuggestedQuestion(SuggestedQuestion scope);
    List<SuggestedQuestion> getSuggestedQuestions(Map<String, Object> params);
    List<SuggestedQuestion> getSuggestedQuestionsV2(Map<String, Object> params);
    long getSuggestedQuestionCount(Map<String, Object> params);
}
