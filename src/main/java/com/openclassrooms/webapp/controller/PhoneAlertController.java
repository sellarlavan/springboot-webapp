package com.openclassrooms.webapp.controller;

import com.openclassrooms.webapp.service.interfaces.PhoneAlertService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/phoneAlert")
public class PhoneAlertController {
    private final PhoneAlertService phoneAlertService;
    private static final Logger logger = LogManager.getLogger(PhoneAlertController.class);

    public PhoneAlertController(PhoneAlertService phoneAlertService) {
        this.phoneAlertService = phoneAlertService;
    }

    @GetMapping
    public ResponseEntity<List<String>> getPhoneNumbersByStation(@RequestParam int firestation) {
        logger.debug("Requête reçue pour récupérer les numéros de téléphone de la station.");
        try {
            List<String> phoneNumbers = phoneAlertService.getPhoneNumbersByStation(firestation);
            if (phoneNumbers == null || phoneNumbers.isEmpty()) {
                logger.info("Aucun numéro trouvé pour la station.");
                return ResponseEntity.notFound().build();
            }
            logger.info("Numéros récupérés avec succès pour la station.");
            return ResponseEntity.ok(phoneNumbers);
        } catch (IOException e) {
            logger.error("Erreur serveur pendant la récupération des numéros pour la station.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
