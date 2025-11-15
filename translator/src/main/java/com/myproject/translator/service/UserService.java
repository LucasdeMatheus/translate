package com.myproject.translator.service;

import com.myproject.translator.domain.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public ResponseEntity<?> findById(Long id) {
        try {
            UserDTO user = userRepository.findLightById(id);
            if (user == null) {
                return ResponseEntity.status(404).body("User not found");
            }
            return ResponseEntity.ok(user);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    public ResponseEntity<?> createUser(UserDTO userDTO) {
        try {
            User user = new User(userDTO);
            userRepository.save(user);
            return ResponseEntity.ok(new UserDTO(user.getId(), user.getEmail(), user.getName()));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("error: " + e);
        }
    }

    public ResponseEntity<?> createLog(Long id, LogDTO logDTO) {
        try{
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            UserLog userLog = new UserLog(logDTO);
            userLog.setUser(user);

            user.addLog(userLog);

            userRepository.save(user);

            return ResponseEntity.ok(true);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("error: " + e);
        }
    }
}
