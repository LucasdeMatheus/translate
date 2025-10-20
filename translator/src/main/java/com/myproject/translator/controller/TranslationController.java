package com.myproject.translator.controller;

import com.myproject.translator.domain.translation.TranslateDTO;
import com.myproject.translator.domain.translation.Translation;
import com.myproject.translator.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/translate")
public class TranslationController {

    @Autowired
    private TranslationService translationService;

    @PostMapping
    public ResponseEntity<List<Translation>> translate(@RequestBody TranslateDTO translateDTO){
        List<Translation> translations = translationService.translate(translateDTO);
        return ResponseEntity.ok(translations);
    }

    @GetMapping
    public ResponseEntity<List<Translation>> tolist(){
        List<Translation> translations = translationService.tolist();
        return ResponseEntity.ok(translations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Translation> getById(@PathVariable("id") Long id){
        Translation translation = translationService.getById(id);
        return ResponseEntity.ok(translation);
    }
}
