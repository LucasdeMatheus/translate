package com.myproject.translator.service;

import com.myproject.translator.domain.translation.TranslateDTO;
import com.myproject.translator.domain.translation.Translation;
import com.myproject.translator.domain.translation.TranslationDTO;
import com.myproject.translator.domain.translation.TranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TranslationService {
    @Autowired
    private TranslationRepository translationRepository;

    @Autowired
    private N8NService n8NService;

    public List<Translation> translate(TranslateDTO translateDTO) {
        // Recebe lista de TranslationDTO do n8n
        List<TranslationDTO> translationDTOs = n8NService.translate(translateDTO);

        // Mapeia para entidades Translation e salva
        List<Translation> translations = translationDTOs.stream()
                .map(dto -> {
                    Translation t = new Translation(dto);
                    translationRepository.save(t);
                    return t;
                })
                .toList();

        return translations;
    }

    public List<Translation> tolist() {
        List<Translation> translations = translationRepository.findAll();
        return translations;
    }

    public Translation getById(Long id) {
        return translationRepository.findById(id)
                .orElse(null); // Retorna null se não encontrar
    }

}
