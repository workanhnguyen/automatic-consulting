package com.nva.server.apis;

import com.nva.server.entities.SuggestedQuestion;
import com.nva.server.services.SuggestedQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/suggestedQuestions")
@CrossOrigin
public class SuggestedQuestionApi {
    @Autowired
    private SuggestedQuestionService suggestedQuestionService;

    @GetMapping
    public ResponseEntity<List<SuggestedQuestion>> getSuggestedQuestions(@RequestParam Map<String, Object> params) {
        return ResponseEntity.ok(suggestedQuestionService.getSuggestedQuestionsV2(params));
    }
}
