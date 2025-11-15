package com.myproject.translator.service;

import com.myproject.translator.domain.dialogue.Dialogue;
import com.myproject.translator.domain.dialogue.DialogueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DialogueService {

    @Autowired
    private DialogueRepository dialogueRepository;

    public List<Dialogue> getDialogue() {
        return dialogueRepository.findAll();
    }

    public Dialogue createDialogue(Dialogue dialogue) {
        return dialogueRepository.save(dialogue);
    }

    /** Busca o texto esperado no banco */
    public String getExpectedText(Long dialogueId) {
        Dialogue dialogue = dialogueRepository.getReferenceById(dialogueId);
        String text = dialogue.getAnswer().trim().toLowerCase();
        System.out.println("Frase esperada: " + text);
        return text;
    }
}
