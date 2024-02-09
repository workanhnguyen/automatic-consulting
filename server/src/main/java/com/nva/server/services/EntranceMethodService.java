package com.nva.server.services;

import com.nva.server.entities.EntranceMethod;

import java.util.List;
import java.util.Map;

public interface EntranceMethodService {
    EntranceMethod saveEntranceMethod(EntranceMethod entranceMethod);
    EntranceMethod editEntranceMethod(EntranceMethod entranceMethod);
    void removeEntranceMethod(EntranceMethod entranceMethod);
    List<EntranceMethod> getEntranceMethods(Map<String, Object> params);
    long getEntranceMethodCount(Map<String, Object> params);
}
