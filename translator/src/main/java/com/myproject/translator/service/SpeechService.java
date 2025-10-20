package com.myproject.translator.service;

import com.myproject.translator.domain.phrase.Phrase;
import com.myproject.translator.domain.phrase.PhraseRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;
import org.vosk.LibVosk;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpeechService {

    @Autowired
    private PhraseRepository phraseRepository;

    public Map<String, String> correction(MultipartFile audioFile, Long phraseId) {
        LocalDateTime startOfRequest = LocalDateTime.now();


        Map<String, String> feedback = new HashMap<>();

        try {

            // --- Busca a frase esperada ---
            Phrase phrase = phraseRepository.getReferenceById(phraseId);
            String expectedText = phrase.getAnswer().trim().toLowerCase();
            System.out.println("Frase esperada: " + expectedText);

            // --- Inicializa Vosk ---
            LibVosk.setLogLevel(LogLevel.INFO);
            Model model = new Model("C:/Users/ffgus/Desktop/translator/translator/src/main/resources/vosk-model-small-en-us-0.15");
            Recognizer recognizer = new Recognizer(model, 16000.0f);

            // --- Converte MultipartFile → AudioInputStream ---
            InputStream bis = new BufferedInputStream(audioFile.getInputStream());
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bis);
            AudioFormat targetFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    16000, 16, 1, 2, 16000, false
            );
            AudioInputStream convertedStream = AudioSystem.getAudioInputStream(targetFormat, audioStream);

            // --- Processa o áudio ---
            byte[] buffer = new byte[4096];
            int bytesRead;
            StringBuilder recognizedTextBuilder = new StringBuilder();

            while ((bytesRead = convertedStream.read(buffer)) != -1) {
                if (recognizer.acceptWaveForm(buffer, bytesRead)) {
                    JSONObject json = new JSONObject(recognizer.getResult());
                    recognizedTextBuilder.append(json.optString("text", "")).append(" ");
                }
            }

            JSONObject finalJson = new JSONObject(recognizer.getFinalResult());
            recognizedTextBuilder.append(finalJson.optString("text", ""));

            // --- Texto final reconhecido ---
            String recognizedText = recognizedTextBuilder.toString().trim().toLowerCase();
            System.out.println("Texto reconhecido (STT): " + recognizedText);

            // --- Fecha recursos ---
            recognizer.close();
            model.close();
            convertedStream.close();
            audioStream.close();

            // --- Comparação literal ---
            if (recognizedText.equals(expectedText)) {
                feedback.put("status", "correct");
                feedback.put("message", "Perfeito! A frase falada corresponde exatamente à esperada.");
            } else {
                feedback.put("status", "incorrect");
                feedback.put("message", "Texto reconhecido: '" + recognizedText + "'");
            }

        } catch (Exception e) {
            e.printStackTrace();
            feedback.put("status", "error");
            feedback.put("message", e.getMessage());
        }
        LocalDateTime endOfRequest = LocalDateTime.now();
        Duration duration = Duration.between(startOfRequest, endOfRequest);

        feedback.put("timesize", duration.toMinutes() + " ms");
        return feedback;
    }

    // --- Utilitários simples ---
    public List<Phrase> getPhrases() {
        return phraseRepository.findAll();
    }

    public Phrase createPhrases(Phrase phrase) {
        return phraseRepository.save(phrase);
    }
}
