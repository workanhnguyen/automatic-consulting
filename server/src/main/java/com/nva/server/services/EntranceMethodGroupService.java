package com.nva.server.services;

import com.nva.server.entities.Action;
import com.nva.server.entities.EntranceMethodGroup;

import java.util.List;
import java.util.Map;

public interface EntranceMethodGroupService {
    EntranceMethodGroup saveEntranceMethodGroup(EntranceMethodGroup entranceMethodGroup);
    EntranceMethodGroup editEntranceMethodGroup(EntranceMethodGroup entranceMethodGroup);
    void removeEntranceMethodGroup(EntranceMethodGroup entranceMethodGroup);
    List<EntranceMethodGroup> getEntranceMethodGroups(Map<String, Object> params);
    long getEntranceMethodGroupCount(Map<String, Object> params);
}
