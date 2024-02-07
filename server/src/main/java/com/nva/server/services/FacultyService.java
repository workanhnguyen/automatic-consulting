package com.nva.server.services;

import com.nva.server.entities.Faculty;
import com.nva.server.entities.User;

import java.util.List;
import java.util.Map;

public interface FacultyService {
    Faculty saveFaculty(Faculty faculty);
    Faculty editFaculty(Faculty faculty);
    void removeFaculty(Faculty faculty);
    List<Faculty> getFaculties(Map<String, Object> params);
    long getFacultyCount(Map<String, Object> params);
}
