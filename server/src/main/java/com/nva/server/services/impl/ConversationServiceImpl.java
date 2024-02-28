package com.nva.server.services.impl;

import com.nva.server.constants.CustomConstants;
import com.nva.server.dtos.ConversationResponse;
import com.nva.server.entities.Conversation;
import com.nva.server.entities.User;
import com.nva.server.exceptions.CommonException;
import com.nva.server.exceptions.DatabaseException;
import com.nva.server.exceptions.EntityExistedException;
import com.nva.server.exceptions.EntityNotFoundException;
import com.nva.server.repositories.ConversationRepository;
import com.nva.server.services.ConversationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class ConversationServiceImpl implements ConversationService {
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private EntityManager entityManager;
    
    @Override
    public Conversation saveConversation(Conversation conversation) {
        try {
            return conversationRepository.save(conversation);
        } catch (Exception e) {
            throw new DatabaseException("Save conversation failed.");
        }
    }
    @Override
    public Conversation editConversation(Conversation conversation) {
        Optional<Conversation> optionalConversation = conversationRepository.findById(conversation.getId());
        if (optionalConversation.isPresent()) {
            try {
                Conversation existingConversation = optionalConversation.get();
                existingConversation.setRequestText(conversation.getRequestText());
                existingConversation.setResponseText(conversation.getResponseText());
                existingConversation.setUser(conversation.getUser());
                existingConversation.setCreatedDate(conversation.getCreatedDate());
                existingConversation.setLastModifiedDate(new Date().getTime());

                return conversationRepository.save(existingConversation);
            } catch (Exception ex) {
                throw new EntityExistedException("Save conversation failed.");
            }
        } else throw new EntityNotFoundException("Conversation is not found.");
    }

    @Override
    public void removeConversation(Conversation conversation) {
        Optional<Conversation> optionalConversation = conversationRepository.findById(conversation.getId());
        if (optionalConversation.isPresent()) {
            conversationRepository.delete(conversation);
        } else throw new EntityNotFoundException("Conversation is not found.");
    }

    @Override
    public List<Conversation> getConversation(Map<String, Object> params) {
        int pageNumber = (int) params.getOrDefault("pageNumber", 0);
        int pageSize = (int) params.getOrDefault("pageSize", CustomConstants.CONVERSATION_PAGE_SIZE);

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));

        String keyword = (String) params.get("keyword");
        if (keyword != null) {
            return conversationRepository.search(keyword, pageable).getContent();
        } else {
            return conversationRepository.findAll(pageable).getContent();
        }
    }

    @Override
    public ConversationResponse getConversationsByUser(Map<String, String> params) {
        try {
            int pageNumber = Integer.parseInt(params.getOrDefault("pageNumber", "1"));
            int pageSize = Integer.parseInt(params.getOrDefault("pageSize", String.valueOf(CustomConstants.CONVERSATION_PAGE_SIZE)));

            if (pageNumber <= 0) pageNumber = 1;
            if (pageSize <= 0) pageSize = 10;

            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
            Root<Conversation> root = criteriaQuery.from(Conversation.class);

            criteriaQuery.multiselect(
                    root.get("requestText").alias("question"),
                    root.get("responseText").alias("answer"),
                    root.get("createdDate").alias("createdDate")
            );

            Predicate predicate = criteriaBuilder.conjunction();

            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

            if (userEmail != null) {
                Join<Conversation, User> userJoin = root.join("user");
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(userJoin.get("email"), userEmail));
            }

            criteriaQuery.where(predicate);
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdDate")));

            List<Tuple> resultList = entityManager.createQuery(criteriaQuery)
                    .setFirstResult((pageNumber - 1) * pageSize)
                    .setMaxResults(pageSize + 1) // Fetch one more to check if there's next page
                    .getResultList();

            ConversationResponse conversationResponse = new ConversationResponse();
            List<Object> data = new ArrayList<>();

            for (Tuple tuple : resultList) {
                Map<String, Object> conversationData = new HashMap<>();
                conversationData.put("question", tuple.get("question"));
                conversationData.put("answer", tuple.get("answer"));
                conversationData.put("createdDate", tuple.get("createdDate"));
                data.add(conversationData);
            }

            boolean hasNext = resultList.size() > pageSize;
            if (hasNext) {
                data = data.subList(0, pageSize); // Adjust sublist condition if there's a next page
            }

            conversationResponse.setData(data);
            conversationResponse.setHasNext(hasNext); // Set hasNext based on the condition

            return conversationResponse;
        } catch (Exception ex) {
            throw new CommonException("Tải cuộc hội thoại thất bại. Vui lòng thử lại sau");
        }
    }




    @Override
    public long getConversationCount(Map<String, Object> params) {
        String keyword = (String) params.get("keyword");

        if (keyword == null || keyword.isEmpty()) {
            return conversationRepository.count();
        } else {
            return conversationRepository.countByKeyword(keyword);
        }
    }
}
