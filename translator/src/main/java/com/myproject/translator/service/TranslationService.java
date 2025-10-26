package com.myproject.translator.service;

import com.myproject.translator.domain.translation.TranslateDTO;
import com.myproject.translator.domain.translation.Translation;
import com.myproject.translator.domain.translation.TranslationDTO;
import com.myproject.translator.domain.translation.TranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TranslationService {
    @Autowired
    private TranslationRepository translationRepository;

    @Autowired
    private N8NService n8NService;

    public List<Translation> translate(TranslateDTO translateDTO) {
        List<String> terms = Arrays.stream(translateDTO.expression().split(","))
                .map(String::trim)
                .toList();

        // Busca termos que realmente não têm tradução próxima
        List<String> missingTerms = getMissingTerms(terms);

        Map<String, Translation> translationMap = new LinkedHashMap<>();

        // Se tiver termos faltantes, chama n8N e salva
        if (!missingTerms.isEmpty()) {
            TranslateDTO missingDTO = new TranslateDTO(String.join(", ", missingTerms), translateDTO.LANGUAGE());
            List<TranslationDTO> translationDTOs = n8NService.translate(missingDTO);

            translationDTOs.forEach(dto -> {
                Translation t = new Translation(dto);
                translationRepository.save(t);
                translationMap.put(t.getText().toLowerCase(), t); // adiciona por texto em minúsculo para evitar duplicatas
            });
        }

        // Pega traduções existentes aproximadas que ainda não foram adicionadas
        translationRepository.findAll().forEach(t -> {
            boolean alreadyAdded = translationMap.containsKey(t.getText().toLowerCase());
            boolean similar = terms.stream().anyMatch(term -> scoreByWords(term.split("\\s+"), t.getText()) >= 0.6);
            if (!alreadyAdded && similar) {
                translationMap.put(t.getText().toLowerCase(), t);
            }
        });

        return new ArrayList<>(translationMap.values());
    }

    /**
     * Retorna os termos que não possuem tradução suficientemente próxima no banco
     */
    private List<String> getMissingTerms(List<String> terms) {
        return terms.stream()
                .filter(term -> {
                    String[] termWords = term.toLowerCase().split("\\s+");

                    // Busca candidatos aproximados no banco
                    List<Translation> candidates = translationRepository.findAll().stream()
                            .filter(t -> {
                                String[] dbWords = t.getText().toLowerCase().split("\\s+");
                                int dbCount = dbWords.length;
                                int termCount = termWords.length;

                                // Aceita ±2 palavras de diferença
                                if (Math.abs(dbCount - termCount) > 2) return false;

                                // Verifica similaridade mínima
                                double score = scoreByWords(termWords, t.getText());
                                return score >= 0.6;
                            })
                            .toList();

                    if (!candidates.isEmpty()) {
                        System.out.println("Traduções parecidas encontradas para '" + term + "': " + candidates.size());
                    }

                    return candidates.isEmpty();
                })
                .toList();
    }

    /**
     * Calcula score de similaridade baseado em palavras comuns
     */
    private double scoreByWords(String[] termWords, String dbText) {
        List<String> dbWords = Arrays.stream(dbText.toLowerCase().split("\\s+"))
                .toList();

        long matches = Arrays.stream(termWords)
                .filter(dbWords::contains)
                .count();

        return (double) matches / Math.max(termWords.length, dbWords.size());
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
