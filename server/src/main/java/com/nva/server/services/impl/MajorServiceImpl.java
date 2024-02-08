package com.nva.server.services.impl;

import com.nva.server.constants.CustomConstants;
import com.nva.server.entities.Faculty;
import com.nva.server.entities.Major;
import com.nva.server.exceptions.EntityExistedException;
import com.nva.server.exceptions.EntityNotFoundException;
import com.nva.server.repositories.FacultyRepository;
import com.nva.server.repositories.MajorRepository;
import com.nva.server.services.MajorService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MajorServiceImpl implements MajorService {
    @Autowired
    private MajorRepository majorRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private EntityManager entityManager;

    @Override
    public Major saveMajor(Major major) {
        List<Major> majorsByFaculty = majorRepository.findByFaculty(major.getFaculty());
        // Major has the same name but different from faculty still can be added
        boolean isMajorExistedInFaculty = majorsByFaculty.stream().anyMatch(item -> item.getName().equalsIgnoreCase(major.getName()));

        if (!isMajorExistedInFaculty)
            return majorRepository.save(major);
        throw new EntityExistedException("This major is already existed in faculty.");
    }

    @Override
    public Major editMajor(Major major) {
        Optional<Major> optionalMajor = majorRepository.findById(major.getId());
        if (optionalMajor.isPresent()) {
            List<Major> majorsByFaculty = majorRepository.findByFaculty(major.getFaculty());
            // Major has the same name but different from faculty still can be added
            boolean isMajorExistedInFaculty = majorsByFaculty.stream().anyMatch(item -> item.getName().equalsIgnoreCase(major.getName()));
            if (!isMajorExistedInFaculty || major.getFaculty().equals(optionalMajor.get().getFaculty())) {
                Major existingMajor = optionalMajor.get();
                existingMajor.setName(major.getName());
                existingMajor.setNote(major.getNote());
                existingMajor.setLastModifiedDate(new Date().getTime());

                return majorRepository.save(existingMajor);
            } else throw new EntityExistedException("This major is already existed in faculty");
        } else throw new EntityNotFoundException("Major is not found.");
    }

    @Override
    public void removeMajor(Major major) {
        Optional<Major> optionalMajor = majorRepository.findById(major.getId());
        if (optionalMajor.isPresent()) {
            majorRepository.deleteById(major.getId());
        } else throw new EntityNotFoundException("Major is not found.");
    }

    @Override
    public List<Major> getMajors(Map<String, Object> params) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Major> criteriaQuery = criteriaBuilder.createQuery(Major.class);
        Root<Major> root = criteriaQuery.from(Major.class);

        Predicate predicate = criteriaBuilder.conjunction();

        String keyword = (String) params.get("keyword");
        if (keyword != null && !keyword.isEmpty()) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("name"), "%" + keyword + "%"));
        }

        String facultyId = (String) params.get("facultyId");
        if (facultyId != null && !facultyId.isEmpty()) {
            Join<Major, Faculty> facultyJoin = root.join("faculty");
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(facultyJoin.get("id"), Long.parseLong(facultyId)));
        }

        criteriaQuery.where(predicate);

        int pageNumber = (int) params.getOrDefault("pageNumber", 0);
        int pageSize = (int) params.getOrDefault("pageSize", CustomConstants.MAJOR_PAGE_SIZE);

        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdDate")));

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public long getMajorCount(Map<String, Object> params) {
        String keyword = (String) params.get("keyword");
        String facultyId = (String) params.get("facultyId");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Major> root = query.from(Major.class);

        Predicate predicate = cb.conjunction();
        if (keyword != null && !keyword.isEmpty()) {
            predicate = cb.and(predicate, cb.like(root.get("name"), "%" + keyword + "%"));
        }
        if (facultyId != null && !facultyId.isEmpty()) {
            Join<Major, Faculty> facultyJoin = root.join("faculty", JoinType.INNER);
            predicate = cb.and(predicate, cb.equal(facultyJoin.get("id"), Long.parseLong(facultyId)));
        }

        query.select(cb.count(root)).where(predicate);

        return entityManager.createQuery(query).getSingleResult();
    }
}
