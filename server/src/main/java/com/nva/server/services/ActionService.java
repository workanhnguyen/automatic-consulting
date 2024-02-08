package com.nva.server.services;

import com.nva.server.entities.Action;
import com.nva.server.entities.Major;

import java.util.List;
import java.util.Map;

public interface ActionService {
    Action saveAction(Action action);
    Action editAction(Action action);
    void removeAction(Action action);
    List<Action> getActions(Map<String, Object> params);
    long getActionCount(Map<String, Object> params);
}
