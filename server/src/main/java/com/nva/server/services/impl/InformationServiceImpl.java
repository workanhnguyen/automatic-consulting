package com.nva.server.services.impl;

import com.nva.server.constants.CustomConstants;
import com.nva.server.entities.Action;
import com.nva.server.entities.Information;
import com.nva.server.entities.Scope;
import com.nva.server.entities.Topic;
import com.nva.server.exceptions.EntityExistedException;
import com.nva.server.exceptions.EntityNotFoundException;
import com.nva.server.repositories.InformationRepository;
import com.nva.server.services.InformationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InformationServiceImpl implements InformationService {
    @Autowired
    private InformationRepository informationRepository;
    @Autowired
    private EntityManager entityManager;

    @Override
    public Information saveInformation(Information information) {
        try {
            return informationRepository.save(information);
        } catch (Exception e) {
            throw new EntityExistedException("Save information failed.");
        }
    }

    @Override
    public Information editInformation(Information information) {
        Optional<Information> optionalInformation = informationRepository.findById(information.getId());
        if (optionalInformation.isPresent()) {
            try {
                Information existingInformation = optionalInformation.get();
                existingInformation.setContent(information.getContent());
                existingInformation.setNote(information.getNote());
                existingInformation.setAction(information.getAction());
                existingInformation.setScope(information.getScope());
                existingInformation.setTopic(information.getTopic());
                existingInformation.setLastModifiedDate(new Date().getTime());

                return informationRepository.save(existingInformation);
            } catch (Exception ex) {
                throw new EntityExistedException("Save information failed.");
            }
        } else throw new EntityNotFoundException("Information is not found.");
    }

    @Override
    public void removeInformation(Information information) {
        Optional<Information> optionalInformation = informationRepository.findById(information.getId());
        if (optionalInformation.isPresent()) {
            informationRepository.delete(information);
        } else throw new EntityNotFoundException("Information is not found.");
    }

    @Override
    public List<Information> getInformation(Map<String, Object> params) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Information> criteriaQuery = criteriaBuilder.createQuery(Information.class);
        Root<Information> root = criteriaQuery.from(Information.class);

        Predicate predicate = criteriaBuilder.conjunction();

        String keyword = (String) params.get("keyword");
        if (keyword != null && !keyword.isEmpty()) {
            predicate = criteriaBuilder.like(root.get("content"), "%" + keyword + "%");
        }

        Long actionId = (Long) params.get("actionId");
        if (actionId != null) {
            Join<Information, Action> actionJoin = root.join("action");
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(actionJoin.get("id"), actionId));
        }

        Long scopeId = (Long) params.get("scopeId");
        if (scopeId != null) {
            Join<Information, Scope> scopeJoin = root.join("scope");
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(scopeJoin.get("id"), scopeId));
        }

        Long topicId = (Long) params.get("topicId");
        if (topicId != null) {
            Join<Information, Topic> topicJoin = root.join("topic");
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(topicJoin.get("id"), topicId));
        }

        criteriaQuery.where(predicate);

        int pageNumber = (int) params.getOrDefault("pageNumber", 0);
        int pageSize = (int) params.getOrDefault("pageSize", CustomConstants.INFORMATION_PAGE_SIZE);

        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdDate")));

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public List<Information> getInformationByIntent(Map<String, String> params) {
        int pageNumber = Integer.parseInt(params.getOrDefault("pageNumber", "0"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", String.valueOf(CustomConstants.INFORMATION_PAGE_SIZE)));

        String action = params.get("action");
        String scope = params.get("scope");
        String topic = params.get("topic");

        if (action != null && !action.isEmpty() &&
                scope != null && !scope.isEmpty() &&
                topic != null && !topic.isEmpty()
        ) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
            return informationRepository.searchByIntent(action, scope, topic, pageable).getContent();
        }
        return new ArrayList<>();
    }


    @Override
    public long getInformationCount(Map<String, Object> params) {
        String keyword = (String) params.get("keyword");

        if (keyword == null || keyword.isEmpty()) {
            return informationRepository.count();
        } else {
            return informationRepository.countByKeyword(keyword);
        }
    }
}
