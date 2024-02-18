package com.nva.server.services.impl;

import com.nva.server.constants.CustomConstants;
import com.nva.server.entities.Information;
import com.nva.server.exceptions.EntityExistedException;
import com.nva.server.exceptions.EntityNotFoundException;
import com.nva.server.repositories.InformationRepository;
import com.nva.server.services.InformationService;
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
            // Adding condition to search in the content field
            predicate = criteriaBuilder.like(root.get("content"), "%" + keyword + "%");
        }

        // Adding condition to exclude EntranceScoreInformation
//        predicate = criteriaBuilder.and(predicate, criteriaBuilder.notEqual(root.type(), EntranceScoreInformation.class));

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
    public long getInformationCount(Map<String, Object> params) {
        String keyword = (String) params.get("keyword");

        if (keyword == null || keyword.isEmpty()) {
            return informationRepository.count();
        } else {
            return informationRepository.countByKeyword(keyword);
        }
    }
}
