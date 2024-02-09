package com.nva.server.services;

import com.nva.server.entities.Topic;

import java.util.List;
import java.util.Map;

public interface TopicService {
    Topic saveTopic(Topic topic);
    Topic editTopic(Topic topic);
    void removeTopic(Topic topic);
    List<Topic> getTopics(Map<String, Object> params);
    long getTopicCount(Map<String, Object> params);
}
