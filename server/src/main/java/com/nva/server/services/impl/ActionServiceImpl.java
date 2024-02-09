package com.nva.server.services.impl;

import com.nva.server.constants.CustomConstants;
import com.nva.server.entities.Action;
import com.nva.server.exceptions.EntityExistedException;
import com.nva.server.exceptions.EntityNotFoundException;
import com.nva.server.repositories.ActionRepository;
import com.nva.server.services.ActionService;
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
public class ActionServiceImpl implements ActionService {
    @Autowired
    private ActionRepository actionRepository;
    @Autowired
    private EntityManager entityManager;

    @Override
    public Action saveAction(Action action) {
        try {
            return actionRepository.save(action);
        } catch (Exception e) {
            throw new EntityExistedException("Action is already existed.");
        }
    }

    @Override
    public Action editAction(Action action) {
        Optional<Action> optionalAction = actionRepository.findById(action.getId());
        if (optionalAction.isPresent()) {
            try {
                Action existingAction = optionalAction.get();
                existingAction.setName(action.getName());
                existingAction.setDescription(action.getDescription());
                existingAction.setNote(action.getNote());
                existingAction.setLastModifiedDate(new Date().getTime());

                return actionRepository.save(existingAction);
            } catch (Exception ex) {
                throw new EntityExistedException("Action name is already existed");
            }
        } else throw new EntityNotFoundException("Action is not found.");
    }

    @Override
    public void removeAction(Action action) {
        Optional<Action> optionalAction = actionRepository.findById(action.getId());
        if (optionalAction.isPresent()) {
            actionRepository.delete(action);
        } else throw new EntityNotFoundException("Action is not found.");
    }

    @Override
    public List<Action> getActions(Map<String, Object> params) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Action> criteriaQuery = criteriaBuilder.createQuery(Action.class);
        Root<Action> root = criteriaQuery.from(Action.class);

        Predicate predicate = criteriaBuilder.conjunction();

        String keyword = (String) params.get("keyword");
        if (keyword != null && !keyword.isEmpty()) {
            // Adding condition to search in both name and description fields
            Predicate namePredicate = criteriaBuilder.like(root.get("name"), "%" + keyword + "%");
            Predicate descriptionPredicate = criteriaBuilder.like(root.get("description"), "%" + keyword + "%");
            predicate = criteriaBuilder.or(namePredicate, descriptionPredicate);
        }

        criteriaQuery.where(predicate);

        int pageNumber = (int) params.getOrDefault("pageNumber", 0);
        int pageSize = (int) params.getOrDefault("pageSize", CustomConstants.ACTION_PAGE_SIZE);

        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdDate")));

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public long getActionCount(Map<String, Object> params) {
        String keyword = (String) params.get("keyword");

        if (keyword == null || keyword.isEmpty()) {
            return actionRepository.count();
        } else {
            return actionRepository.countByKeyword(keyword);
        }
    }
}
