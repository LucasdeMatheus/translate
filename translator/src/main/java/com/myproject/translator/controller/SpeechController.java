package com.myproject.translator.controller;

import com.myproject.translator.service.SpeechService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/speech")
public class SpeechController {


    @Autowired
    private SpeechService speechService;




    @PostMapping("/stt")
    public ResponseEntity<Map<String, String>> correction(
            @RequestParam("audio") MultipartFile audioFile,
            @RequestParam("phraseId") Long phraseId
    ){
        return ResponseEntity.ok(speechService.correction(audioFile, phraseId, "dialogue"));
    }


}
