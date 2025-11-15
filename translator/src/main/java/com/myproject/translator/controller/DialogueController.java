package com.myproject.translator.controller;

import com.myproject.translator.domain.dialogue.Dialogue;
import com.myproject.translator.service.DialogueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dialogue")
public class DialogueController {

    @Autowired
    private DialogueService dialogueService;

    @GetMapping
    public ResponseEntity<List<Dialogue>> getDialogue(){
        return ResponseEntity.ok(dialogueService.getDialogue());
    }

    @PostMapping
    public ResponseEntity<Dialogue> createDialogue(@RequestBody Dialogue dialogue){
        return ResponseEntity.ok(dialogueService.createDialogue(dialogue));
    }
}
