package com.nva.server.services.impl;

import com.nva.server.constants.CustomConstants;
import com.nva.server.entities.Scope;
import com.nva.server.entities.Topic;
import com.nva.server.exceptions.EntityExistedException;
import com.nva.server.exceptions.EntityNotFoundException;
import com.nva.server.repositories.ScopeRepository;
import com.nva.server.repositories.TopicRepository;
import com.nva.server.services.TopicService;
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
public class TopicServiceImpl implements TopicService {
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private EntityManager entityManager;

    @Override
    public Topic saveTopic(Topic topic) {
        Optional<Topic> optionalTopic = topicRepository.findByName(topic.getName());
        if (optionalTopic.isEmpty())
            return topicRepository.save(topic);
        throw new EntityExistedException("Topic name is already existed.");
    }

    @Override
    public Topic editTopic(Topic topic) {
        Optional<Topic> optionalTopic = topicRepository.findById(topic.getId());
        if (optionalTopic.isPresent()) {
            if (topicRepository.findByName(topic.getName()).isEmpty()) {
                Topic existingTopic = optionalTopic.get();
                existingTopic.setName(topic.getName());
                existingTopic.setDescription(topic.getDescription());
                existingTopic.setNote(topic.getNote());
                existingTopic.setLastModifiedDate(new Date().getTime());

                return topicRepository.save(existingTopic);
            } else throw new EntityExistedException("Topic name is already existed.");
        } else throw new EntityNotFoundException("Topic is not found.");
    }

    @Override
    public void removeTopic(Topic topic) {
        Optional<Topic> optionalTopic = topicRepository.findById(topic.getId());
        if (optionalTopic.isPresent()) {
            topicRepository.delete(topic);
        } else throw new EntityNotFoundException("Topic is not found.");
    }

    @Override
    public List<Topic> getTopics(Map<String, Object> params) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Topic> criteriaQuery = criteriaBuilder.createQuery(Topic.class);
        Root<Topic> root = criteriaQuery.from(Topic.class);

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
    public long getTopicCount(Map<String, Object> params) {
        String keyword = (String) params.get("keyword");

        if (keyword == null || keyword.isEmpty()) {
            return topicRepository.count();
        } else {
            return topicRepository.countByKeyword(keyword);
        }
    }
}
