package com.nva.server.services.impl;

import com.nva.server.constants.CustomConstants;
import com.nva.server.entities.Conversation;
import com.nva.server.exceptions.DatabaseException;
import com.nva.server.exceptions.EntityExistedException;
import com.nva.server.exceptions.EntityNotFoundException;
import com.nva.server.repositories.ConversationRepository;
import com.nva.server.services.ConversationService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        String keyword = (String) params.get("keyword");
        int pageNumber = (int) params.getOrDefault("pageNumber", 0);
        int pageSize = (int) params.getOrDefault("pageSize", CustomConstants.CONVERSATION_PAGE_SIZE);

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));

        if (keyword == null) {
            return conversationRepository.findAll(pageable).getContent();
        } else {
            return conversationRepository.search(keyword, pageable).getContent();
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
