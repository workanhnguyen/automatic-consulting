package com.nva.server.services.impl;

import com.nva.server.constants.CustomConstants;
import com.nva.server.entities.SuggestedQuestion;
import com.nva.server.exceptions.DatabaseException;
import com.nva.server.exceptions.EntityNotFoundException;
import com.nva.server.repositories.SuggestedQuestionRepository;
import com.nva.server.services.SuggestedQuestionService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class SuggestedQuestionServiceImpl implements SuggestedQuestionService {
    @Autowired
    private SuggestedQuestionRepository suggestedQuestionRepository;
    @Autowired
    private EntityManager entityManager;

    @Override
    public SuggestedQuestion saveSuggestedQuestion(SuggestedQuestion suggestedQuestion) {
        try {
            return suggestedQuestionRepository.save(suggestedQuestion);
        } catch (Exception e) {
            throw new DatabaseException("Save suggested question failed.");
        }
    }

    @Override
    public SuggestedQuestion editSuggestedQuestion(SuggestedQuestion suggestedQuestion) {
        Optional<SuggestedQuestion> optionalSuggestedQuestion = suggestedQuestionRepository.findById(suggestedQuestion.getId());
        if (optionalSuggestedQuestion.isPresent()) {
            try {
                SuggestedQuestion existingSuggestedQuestion = optionalSuggestedQuestion.get();
                existingSuggestedQuestion.setContent(suggestedQuestion.getContent());
                existingSuggestedQuestion.setNote(suggestedQuestion.getNote());
                existingSuggestedQuestion.setLastModifiedDate(new Date().getTime());

                return suggestedQuestionRepository.save(existingSuggestedQuestion);
            } catch (Exception e) {
                throw new DatabaseException("Edit suggested question failed.");
            }
        } else throw new EntityNotFoundException("Suggested question is not found.");
    }

    @Override
    public void removeSuggestedQuestion(SuggestedQuestion suggestedQuestion) {
        Optional<SuggestedQuestion> optionalSuggestedQuestion = suggestedQuestionRepository.findById(suggestedQuestion.getId());
        if (optionalSuggestedQuestion.isPresent()) {
            suggestedQuestionRepository.delete(suggestedQuestion);
        } else throw new EntityNotFoundException("Suggested question is not found.");
    }

    @Override
    public List<SuggestedQuestion> getSuggestedQuestions(Map<String, Object> params) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SuggestedQuestion> criteriaQuery = criteriaBuilder.createQuery(SuggestedQuestion.class);
        Root<SuggestedQuestion> root = criteriaQuery.from(SuggestedQuestion.class);

        Predicate predicate = criteriaBuilder.conjunction(); // Or can use List<Predicate> instead.

        String keyword = (String) params.get("keyword");
        if (keyword != null && !keyword.isEmpty()) {
            // Adding condition to search in both name and description fields
            Predicate contentPredicate = criteriaBuilder.like(root.get("content"), "%" + keyword + "%");
            predicate = criteriaBuilder.and(predicate, contentPredicate);
        }

        criteriaQuery.where(predicate);

        int pageNumber = (int) params.getOrDefault("pageNumber", 0);
        int pageSize = (int) params.getOrDefault("pageSize", CustomConstants.SUGGESTED_QUESTION_PAGE_SIZE);

        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdDate")));

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public List<SuggestedQuestion> getSuggestedQuestionsV2(Map<String, Object> params) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SuggestedQuestion> criteriaQuery = criteriaBuilder.createQuery(SuggestedQuestion.class);
        Root<SuggestedQuestion> root = criteriaQuery.from(SuggestedQuestion.class);

        Predicate predicate = criteriaBuilder.conjunction(); // Or can use List<Predicate> instead.

        String keyword = (String) params.get("keyword");
        if (keyword != null && !keyword.isEmpty()) {
            // Adding condition to search in both name and description fields
            Predicate contentPredicate = criteriaBuilder.like(root.get("content"), "%" + keyword + "%");
            predicate = criteriaBuilder.and(predicate, contentPredicate);
        }

        criteriaQuery.where(predicate);

        int pageNumber = Integer.parseInt((String) params.getOrDefault("pageNumber", "0"));
        int pageSize = Integer.parseInt((String) params.getOrDefault("pageSize", String.valueOf(CustomConstants.SUGGESTED_QUESTION_PAGE_SIZE)));

        if (pageNumber <= 0) pageNumber = 1;
        if (pageSize <= 0) pageSize = 10;

        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdDate")));

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public long getSuggestedQuestionCount(Map<String, Object> params) {
        String keyword = (String) params.get("keyword");

        if (keyword == null || keyword.isEmpty()) {
            return suggestedQuestionRepository.count();
        } else {
            return suggestedQuestionRepository.countByKeyword(keyword);
        }
    }
}
