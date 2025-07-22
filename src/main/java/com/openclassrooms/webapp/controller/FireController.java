package com.openclassrooms.webapp.controller;

import com.openclassrooms.webapp.dto.FireDTO;
import com.openclassrooms.webapp.service.FireService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fire")
public class FireController {

    private final FireService fireService;

    public FireController(FireService fireService) {
        this.fireService = fireService;
    }

    @GetMapping
    public ResponseEntity<?> getFireInfo(@RequestParam String address) {
        try {
            FireDTO result = fireService.getResidentsAndStation(address);

            if (result.getPersons().isEmpty()) {
                return ResponseEntity.ok("Aucun résident trouvé à cette adresse.");
            }

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur serveur");
        }
    }
}
