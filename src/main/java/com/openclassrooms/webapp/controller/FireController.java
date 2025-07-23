package com.openclassrooms.webapp.controller;

import com.openclassrooms.webapp.dto.FireDTO;
import com.openclassrooms.webapp.service.interfaces.FireService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/fire")
public class FireController {

    private final FireService fireService;
    private static final Logger logger = LogManager.getLogger(FireController.class);

    public FireController(FireService fireService) {
        this.fireService = fireService;
    }

    @GetMapping
    public ResponseEntity<?> getFireInfo(@RequestParam String address) {
        logger.debug("Requête reçue pour les infos incendie à l'adresse.");

        try {
            FireDTO result = fireService.getResidentsAndStation(address);

            if (result == null) {
                logger.error("Adresse inexistante.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            if (result.getPersons().isEmpty()) {
                logger.info("Adresse connue pour une caserne mais aucun habitant.");
                return ResponseEntity.ok().build();
            }

            logger.info("Résidents trouvés à l'adresse.");
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            logger.error("Erreur serveur lors de la récupération des infos incendie pour l'adresse.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
