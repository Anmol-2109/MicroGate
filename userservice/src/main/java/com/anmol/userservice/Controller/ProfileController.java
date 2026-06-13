package com.anmol.userservice.Controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class ProfileController {

    @GetMapping
    public ResponseEntity<?> seeProfile(){
        return ResponseEntity.ok("My profile returned");
    }
}
