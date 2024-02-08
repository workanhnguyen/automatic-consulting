package com.nva.server.services;

import com.nva.server.entities.Major;

import java.util.List;
import java.util.Map;

public interface MajorService {
    Major saveMajor(Major major);
    Major editMajor(Major major);
    void removeMajor(Major major);
    List<Major> getMajors(Map<String, Object> params);
    long getMajorCount(Map<String, Object> params);
}
