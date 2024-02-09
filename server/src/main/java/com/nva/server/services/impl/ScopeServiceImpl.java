package com.nva.server.services.impl;

import com.nva.server.constants.CustomConstants;
import com.nva.server.entities.Scope;
import com.nva.server.exceptions.EntityExistedException;
import com.nva.server.exceptions.EntityNotFoundException;
import com.nva.server.repositories.ScopeRepository;
import com.nva.server.services.ScopeService;
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
public class ScopeServiceImpl implements ScopeService {
    @Autowired
    private ScopeRepository scopeRepository;
    @Autowired
    private EntityManager entityManager;

    @Override
    public Scope saveScope(Scope scope) {
        try {
            return scopeRepository.save(scope);
        } catch (Exception e) {
            throw new EntityExistedException("Scope is already existed.");
        }
    }

    @Override
    public Scope editScope(Scope scope) {
        Optional<Scope> optionalScope = scopeRepository.findById(scope.getId());
        if (optionalScope.isPresent()) {
            try {
                Scope existingScope = optionalScope.get();
                existingScope.setName(scope.getName());
                existingScope.setDescription(scope.getDescription());
                existingScope.setNote(scope.getNote());
                existingScope.setLastModifiedDate(new Date().getTime());

                return scopeRepository.save(existingScope);
            } catch (Exception e) {
                throw new EntityExistedException("Scope name is already existed.");
            }
        } else throw new EntityNotFoundException("Scope is not found.");
    }

    @Override
    public void removeScope(Scope scope) {
        Optional<Scope> optionalScope = scopeRepository.findById(scope.getId());
        if (optionalScope.isPresent()) {
            scopeRepository.delete(scope);
        } else throw new EntityNotFoundException("Scope is not found.");
    }

    @Override
    public List<Scope> getScopes(Map<String, Object> params) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Scope> criteriaQuery = criteriaBuilder.createQuery(Scope.class);
        Root<Scope> root = criteriaQuery.from(Scope.class);

        Predicate predicate = criteriaBuilder.conjunction(); // Or can use List<Predicate> instead.

        String keyword = (String) params.get("keyword");
        if (keyword != null && !keyword.isEmpty()) {
            // Adding condition to search in both name and description fields
            Predicate namePredicate = criteriaBuilder.like(root.get("name"), "%" + keyword + "%");
            Predicate descriptionPredicate = criteriaBuilder.like(root.get("description"), "%" + keyword + "%");
            predicate = criteriaBuilder.or(namePredicate, descriptionPredicate);
        }

        criteriaQuery.where(predicate);

        int pageNumber = (int) params.getOrDefault("pageNumber", 0);
        int pageSize = (int) params.getOrDefault("pageSize", CustomConstants.SCOPE_PAGE_SIZE);

        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdDate")));

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public long getScopeCount(Map<String, Object> params) {
        String keyword = (String) params.get("keyword");

        if (keyword == null || keyword.isEmpty()) {
            return scopeRepository.count();
        } else {
            return scopeRepository.countByKeyword(keyword);
        }
    }
}
