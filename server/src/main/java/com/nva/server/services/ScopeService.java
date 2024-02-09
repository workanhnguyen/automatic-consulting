package com.nva.server.services;

import com.nva.server.entities.Scope;

import java.util.List;
import java.util.Map;

public interface ScopeService {
    Scope saveScope(Scope scope);
    Scope editScope(Scope scope);
    void removeScope(Scope scope);
    List<Scope> getScopes(Map<String, Object> params);
    long getScopeCount(Map<String, Object> params);
}
