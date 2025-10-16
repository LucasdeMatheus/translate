package com.myproject.translator.service;

import com.myproject.translator.domain.question.Question;
import com.myproject.translator.domain.question.QuestionDTO;
import com.myproject.translator.domain.question.QuestionRepository;
import com.myproject.translator.domain.translation.Translation;
import com.myproject.translator.domain.translation.TranslationDTO;
import com.myproject.translator.domain.translation.TranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TranslationRepository translationRepository;

    @Autowired
    private N8NService n8NService;

    public TranslationDTO questions(Long id) {
        // Recupera a tradução pelo ID
        Translation translation = translationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Translation não encontrada"));

        // Gera as questões via n8n
        TranslationDTO translationDTO = n8NService.gerarQuestoes(translation);

        // Salva cada questão no repositório associada à tradução
        if (translationDTO.questions() != null) {
            List<Question> questions = translationDTO.questions().stream()
                    .map(qdto -> new Question(qdto, translation)) // Construtor Question(QuestionDTO, Translation)
                    .toList();

            questionRepository.saveAll(questions); // Salva todas de uma vez
        }

        return translationDTO;
    }

    public QuestionDTO getQuestion(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Questão não encontrada"));

        // Converte para DTO
        return new QuestionDTO(
                question.getDescription(),
                question.getAlternatives(),
                question.getCorrectAnswer(),
                question.getExplanation()
        );
    }


    public List<QuestionDTO> getQuestions() {
        List<Question> questions = questionRepository.findAll();

        // Converte cada Question em QuestionDTO
        return questions.stream()
                .map(q -> new QuestionDTO(
                        q.getDescription(),
                        q.getAlternatives(),
                        q.getCorrectAnswer(),
                        q.getExplanation()
                ))
                .toList();
    }



}
