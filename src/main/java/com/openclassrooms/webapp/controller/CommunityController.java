package com.openclassrooms.webapp.controller;

import com.openclassrooms.webapp.dto.CommunityEmailDTO;
import com.openclassrooms.webapp.service.CommunityService;
import com.openclassrooms.webapp.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/communityEmail")
public class CommunityController {

    private final CommunityService communityService;

    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @GetMapping
    public ResponseEntity<?> getCommunityEmail(@RequestParam String city) {
        try {
            CommunityEmailDTO result = communityService.getEmailsByCity(city);
            if (result.getEmails().isEmpty()) {
                return ResponseEntity.ok("Aucune adresse e-mail trouvée pour cette ville.");
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur serveur");
        }
    }
}