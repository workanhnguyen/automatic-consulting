package com.nva.server.services;

import com.nva.server.entities.Information;

import java.util.List;
import java.util.Map;

public interface InformationService {
    Information saveInformation(Information Information);
    Information editInformation(Information Information);
    void removeInformation(Information Information);
    List<Information> getInformation(Map<String, Object> params);
    long getInformationCount(Map<String, Object> params);
}
