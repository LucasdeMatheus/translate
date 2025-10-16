package com.myproject.translator.controller;

import com.myproject.translator.domain.question.Question;
import com.myproject.translator.domain.question.QuestionDTO;
import com.myproject.translator.domain.translation.TranslationDTO;
import com.myproject.translator.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;


    @PostMapping("/{id}")
    public ResponseEntity<TranslationDTO> questions(@PathVariable("id")Long id){
        return ResponseEntity.ok(questionService.questions(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestion(@PathVariable("id")Long id){
        return ResponseEntity.ok(questionService.getQuestion(id));
    }

    @GetMapping
    public ResponseEntity<List<QuestionDTO>> getQuestions() {
        return ResponseEntity.ok(questionService.getQuestions());
    }

}
