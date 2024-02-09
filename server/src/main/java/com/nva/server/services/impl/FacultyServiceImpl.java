package com.nva.server.services.impl;

import com.nva.server.constants.CustomConstants;
import com.nva.server.entities.Faculty;
import com.nva.server.exceptions.EntityExistedException;
import com.nva.server.exceptions.EntityNotFoundException;
import com.nva.server.repositories.FacultyRepository;
import com.nva.server.services.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FacultyServiceImpl implements FacultyService {
    @Autowired
    private FacultyRepository facultyRepository;

    @Override
    public Faculty saveFaculty(Faculty faculty) {
        try {
            return facultyRepository.save(faculty);
        } catch (Exception e) {
            throw new EntityExistedException("Faculty is already existed.");
        }
    }

    @Override
    public Faculty editFaculty(Faculty faculty) {
        Optional<Faculty> optionalFaculty = facultyRepository.findById(faculty.getId());
        if (optionalFaculty.isPresent()) {
            try {
                Faculty existingFaculty = optionalFaculty.get();
                existingFaculty.setName(faculty.getName());
                existingFaculty.setNote(faculty.getNote());
                existingFaculty.setLastModifiedDate(new Date().getTime());

                return facultyRepository.save(existingFaculty);
            } catch (Exception e) {
                throw new EntityExistedException("Faculty name is already existed.");
            }
        } else throw new EntityNotFoundException("Faculty is not found.");
    }

    @Override
    public void removeFaculty(Faculty faculty) {
        Optional<Faculty> optionalFaculty = facultyRepository.findByName(faculty.getName());
        if (optionalFaculty.isPresent()) {
            facultyRepository.delete(faculty);
        } else throw new EntityNotFoundException("Faculty is not found.");
    }

    @Override
    public List<Faculty> getFaculties(Map<String, Object> params) {
        String keyword = (String) params.get("keyword");
        int pageNumber = (int) params.getOrDefault("pageNumber", 0);
        int pageSize = (int) params.getOrDefault("pageSize", CustomConstants.FACULTY_PAGE_SIZE);

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));

        if (keyword == null || keyword.isEmpty()) {
            return facultyRepository.findAll(pageable).getContent();
        } else {
            return facultyRepository.search(keyword, pageable).getContent();
        }
    }

    @Override
    public long getFacultyCount(Map<String, Object> params) {
        String keyword = (String) params.get("keyword");

        if (keyword == null) {
            return facultyRepository.count();
        } else {
            return facultyRepository.countByKeyword(keyword);
        }
    }
}
