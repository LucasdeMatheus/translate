package com.myproject.translator.service;

import com.myproject.translator.domain.dialogue.Dialogue;
import com.myproject.translator.domain.dialogue.DialogueRepository;
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
import java.util.Map;

@Service
public class SpeechService {

    @Autowired
    private DialogueRepository dialogueRepository;

    @Autowired
    private DialogueService dialogueService;

    public Map<String, String> correction(MultipartFile audioFile, Long id, String type) {
        Map<String, String> feedback = new HashMap<>();

        try {
            String expectedText = "";

            if (type == "dialogue") {
                expectedText = dialogueService.getExpectedText(id);
            } else if (type == "phraese") {
               // expectedText = getExpectedText(id);
            }

            String recognizedText = recognizeSpeech(audioFile);

            compareResults(expectedText, recognizedText, feedback);

        } catch (Exception e) {
            e.printStackTrace();
            feedback.put("status", "error");
            feedback.put("message", e.getMessage());
        }

        return feedback;
    }

    // ---------------- MÉTODOS AUXILIARES ---------------- //



    /** Processa o áudio com Vosk e retorna o texto reconhecido */
    private String recognizeSpeech(MultipartFile audioFile) throws Exception {
        LibVosk.setLogLevel(LogLevel.INFO);
        Model model = new Model("C:/Users/ffgus/Desktop/translator/translator/src/main/resources/vosk-model-small-en-us-0.15");
        Recognizer recognizer = new Recognizer(model, 16000.0f);

        InputStream bis = new BufferedInputStream(audioFile.getInputStream());
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(bis);

        AudioFormat targetFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                16000, 16, 1, 2, 16000, false
        );
        AudioInputStream convertedStream = AudioSystem.getAudioInputStream(targetFormat, audioStream);

        StringBuilder recognizedTextBuilder = new StringBuilder();
        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = convertedStream.read(buffer)) != -1) {
            if (recognizer.acceptWaveForm(buffer, bytesRead)) {
                JSONObject json = new JSONObject(recognizer.getResult());
                recognizedTextBuilder.append(json.optString("text", "")).append(" ");
            }
        }

        JSONObject finalJson = new JSONObject(recognizer.getFinalResult());
        recognizedTextBuilder.append(finalJson.optString("text", ""));

        // Fechamento
        recognizer.close();
        model.close();
        convertedStream.close();
        audioStream.close();

        String recognizedText = recognizedTextBuilder.toString().trim().toLowerCase();
        System.out.println("Texto reconhecido (STT): " + recognizedText);
        return recognizedText;
    }

    /** Compara os textos e adiciona o resultado no mapa de feedback */
    private void compareResults(String expected, String recognized, Map<String, String> feedback) {
        if (recognized.equals(expected)) {
            feedback.put("status", "correct");
            feedback.put("message", "Perfeito! A frase falada corresponde exatamente à esperada.");
        } else {
            feedback.put("status", "incorrect");
            feedback.put("message", "Texto reconhecido: '" + recognized + "'");
        }
    }
}
