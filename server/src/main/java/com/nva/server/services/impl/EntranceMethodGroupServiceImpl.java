package com.nva.server.services.impl;

import com.nva.server.constants.CustomConstants;
import com.nva.server.entities.EntranceMethodGroup;
import com.nva.server.exceptions.DatabaseException;
import com.nva.server.exceptions.EntityNotFoundException;
import com.nva.server.repositories.EntranceMethodGroupRepository;
import com.nva.server.services.EntranceMethodGroupService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EntranceMethodGroupServiceImpl implements EntranceMethodGroupService {
    @Autowired
    private EntranceMethodGroupRepository entranceMethodGroupRepository;
    @Autowired
    private EntityManager entityManager;

    @Override
    public EntranceMethodGroup saveEntranceMethodGroup(EntranceMethodGroup entranceMethodGroup) {
        try {
            return entranceMethodGroupRepository.save(entranceMethodGroup);
        } catch (Exception e) {
            throw new DatabaseException("Save entrance method group failed.");
        }
    }

    @Override
    public EntranceMethodGroup editEntranceMethodGroup(EntranceMethodGroup entranceMethodGroup) {
        Optional<EntranceMethodGroup> optionalEntranceMethodGroup = entranceMethodGroupRepository.findById(entranceMethodGroup.getId());
        if (optionalEntranceMethodGroup.isPresent()) {
            try {
                EntranceMethodGroup existingEntranceMethodGroup = optionalEntranceMethodGroup.get();
                existingEntranceMethodGroup.setName(entranceMethodGroup.getName());
                existingEntranceMethodGroup.setNote(entranceMethodGroup.getNote());
                existingEntranceMethodGroup.setLastModifiedDate(new Date().getTime());

                return entranceMethodGroupRepository.save(existingEntranceMethodGroup);
            } catch (Exception ex) {
                throw new DatabaseException("Edit entrance method group failed.");
            }
        } else throw new EntityNotFoundException("Entrance method group is not found.");
    }

    @Override
    public void removeEntranceMethodGroup(EntranceMethodGroup entranceMethodGroup) {
        Optional<EntranceMethodGroup> optionalEntranceMethodGroup = entranceMethodGroupRepository.findById(entranceMethodGroup.getId());
        if (optionalEntranceMethodGroup.isPresent()) {
            entranceMethodGroupRepository.delete(entranceMethodGroup);
        } else throw new EntityNotFoundException("Entrance method group is not found.");
    }

    @Override
    public List<EntranceMethodGroup> getEntranceMethodGroups(Map<String, Object> params) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<EntranceMethodGroup> criteriaQuery = criteriaBuilder.createQuery(EntranceMethodGroup.class);
        Root<EntranceMethodGroup> root = criteriaQuery.from(EntranceMethodGroup.class);

        Predicate predicate = criteriaBuilder.conjunction();

        String keyword = (String) params.get("keyword");
        if (keyword != null && !keyword.isEmpty()) {
            // Adding condition to search in both name and description fields
            Predicate namePredicate = criteriaBuilder.like(root.get("name"), "%" + keyword + "%");
            predicate = criteriaBuilder.or(namePredicate);
        }

        criteriaQuery.where(predicate);

        int pageNumber = (int) params.getOrDefault("pageNumber", 0);
        int pageSize = (int) params.getOrDefault("pageSize", CustomConstants.ENTRANCE_METHOD_GROUP_PAGE_SIZE);

        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdDate")));

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public long getEntranceMethodGroupCount(Map<String, Object> params) {
        String keyword = (String) params.get("keyword");

        if (keyword == null || keyword.isEmpty()) {
            return entranceMethodGroupRepository.count();
        } else {
            return entranceMethodGroupRepository.countByKeyword(keyword);
        }
    }
}
