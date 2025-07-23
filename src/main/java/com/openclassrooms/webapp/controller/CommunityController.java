package com.openclassrooms.webapp.controller;

import com.openclassrooms.webapp.dto.CommunityEmailDTO;
import com.openclassrooms.webapp.service.interfaces.CommunityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/communityEmail")
public class CommunityController {

    private final CommunityService communityService;
    private static final Logger logger = LogManager.getLogger(CommunityController.class);

    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @GetMapping
    public ResponseEntity<?> getCommunityEmail(@RequestParam String city) {
        logger.debug("Requête reçue pour récupérer les emails de la ville.");
        try {
            CommunityEmailDTO result = communityService.getEmailsByCity(city);
            if (result.getEmails().isEmpty()) {
                logger.error("Aucune adresse e-mail trouvée pour la ville.");
                return ResponseEntity.ok().build();
            }
            logger.info("Emails récupérés avec succès pour la ville.");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur serveur lors de la récupération des emails pour la ville.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}