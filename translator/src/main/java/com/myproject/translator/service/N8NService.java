package com.myproject.translator.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.translator.domain.question.QuestionDTO;
import com.myproject.translator.domain.translation.TranslateDTO;
import com.myproject.translator.domain.translation.Translation;
import com.myproject.translator.domain.translation.TranslationDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class N8NService {

    private final WebClient webClient;

    public N8NService() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:5678") // URL do seu n8n
                .build();
    }


    public List<TranslationDTO> translate(TranslateDTO translateDTO) {
        Map<String, Object> body = Map.of(
                "chatInput", translateDTO.expression(),
                "targetLang", translateDTO.LANGUAGE()
        );

        Mono<String> response = webClient.post()
                .uri("/webhook/traduzir")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class);

        String resultado = response.block();
        System.out.println("Resposta do n8n: " + resultado);

        try {
            // Desserializa como lista
            List<Map<String, Object>> lista = objectMapper.readValue(resultado, new TypeReference<List<Map<String, Object>>>() {});

            List<TranslationDTO> translationDTOs = new ArrayList<>();

            if (!lista.isEmpty() && lista.get(0).containsKey("translations")) {
                List<Map<String, Object>> translations = (List<Map<String, Object>>) lista.get(0).get("translations");

                for (Map<String, Object> t : translations) {
                    translationDTOs.add(new TranslationDTO(
                            (String) t.get("expression"),
                            (List<QuestionDTO>) t.getOrDefault("questions", List.of()),
                            (String) t.get("literal"),
                            (String) t.get("natural"),
                            (String) t.get("explanation")
                    ));
                }
            }

            return translationDTOs;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter resposta do n8n para TranslationDTO", e);
        }
    }



    private final ObjectMapper objectMapper = new ObjectMapper();

    public TranslationDTO gerarQuestoes(Translation translation) {
        Map<String, Object> body = Map.of("expressao", translation);

        // Chamada ao webhook
        Mono<String> response = webClient.post()
                .uri("/webhook/question")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class);

        String resultado = response.block();
        System.out.println("Resposta bruta do n8n: " + resultado);

        try {
            // Desserializa como lista
            List<TranslationDTO> lista = objectMapper.readValue(
                    resultado, new TypeReference<List<TranslationDTO>>() {}
            );

            // Retorna apenas o primeiro item da lista, ou lança exceção se vazio
            if (!lista.isEmpty()) {
                return lista.get(0);
            } else {
                throw new RuntimeException("Nenhuma questão retornada pelo n8n");
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao parsear resposta do n8n", e);
        }
    }




}
