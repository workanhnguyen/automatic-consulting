package com.nva.server.services.impl;

import com.nva.server.constants.CustomConstants;
import com.nva.server.entities.EntranceMethod;
import com.nva.server.exceptions.DatabaseException;
import com.nva.server.exceptions.EntityNotFoundException;
import com.nva.server.repositories.EntranceMethodRepository;
import com.nva.server.services.EntranceMethodService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class EntranceMethodServiceImpl implements EntranceMethodService {
    @Autowired
    private EntranceMethodRepository entranceMethodRepository;
    @Autowired
    private EntityManager entityManager;

    @Override
    public EntranceMethod saveEntranceMethod(EntranceMethod entranceMethod) {
        try {
            return entranceMethodRepository.save(entranceMethod);
        } catch (Exception e) {
            throw new DatabaseException("Save entrance method  failed.");
        }
    }

    @Override
    public EntranceMethod editEntranceMethod(EntranceMethod entranceMethod) {
        Optional<EntranceMethod> optionalEntranceMethod = entranceMethodRepository.findById(entranceMethod.getId());
        if (optionalEntranceMethod.isPresent()) {
            try {
                EntranceMethod existingEntranceMethod = optionalEntranceMethod.get();
                existingEntranceMethod.setName(entranceMethod.getName());
                existingEntranceMethod.setNote(entranceMethod.getNote());
                existingEntranceMethod.setEntranceMethodGroup(entranceMethod.getEntranceMethodGroup());
                existingEntranceMethod.setLastModifiedDate(new Date().getTime());

                return entranceMethodRepository.save(existingEntranceMethod);
            } catch (Exception ex) {
                throw new DatabaseException("Edit entrance method  failed.");
            }
        } else throw new EntityNotFoundException("Entrance method  is not found.");
    }

    @Override
    public void removeEntranceMethod(EntranceMethod entranceMethod) {
        Optional<EntranceMethod> optionalEntranceMethod = entranceMethodRepository.findById(entranceMethod.getId());
        if (optionalEntranceMethod.isPresent()) {
            entranceMethodRepository.delete(entranceMethod);
        } else throw new EntityNotFoundException("Entrance method  is not found.");
    }

    @Override
    public List<EntranceMethod> getEntranceMethods(Map<String, Object> params) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<EntranceMethod> criteriaQuery = criteriaBuilder.createQuery(EntranceMethod.class);
        Root<EntranceMethod> root = criteriaQuery.from(EntranceMethod.class);

        Predicate predicate = criteriaBuilder.conjunction();

        String keyword = (String) params.get("keyword");
        if (keyword != null && !keyword.isEmpty()) {
            // Adding condition to search in both name and description fields
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("name"), "%" + keyword + "%"));
        }

        criteriaQuery.where(predicate);

        int pageNumber = (int) params.getOrDefault("pageNumber", 0);
        int pageSize = (int) params.getOrDefault("pageSize", CustomConstants.ENTRANCE_METHOD_PAGE_SIZE);

        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdDate")));

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public long getEntranceMethodCount(Map<String, Object> params) {
        String keyword = (String) params.get("keyword");

        if (keyword == null || keyword.isEmpty()) {
            return entranceMethodRepository.count();
        } else {
            return entranceMethodRepository.countByKeyword(keyword);
        }
    }
}
