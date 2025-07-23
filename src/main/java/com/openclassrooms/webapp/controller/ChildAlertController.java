package com.openclassrooms.webapp.controller;

import com.openclassrooms.webapp.dto.ChildrenAtAddressDTO;
import com.openclassrooms.webapp.service.interfaces.ChildAlertService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/childAlert")
public class ChildAlertController {
    private final ChildAlertService childAlertService;
    private static final Logger logger = LogManager.getLogger(ChildAlertController.class);

    public ChildAlertController(ChildAlertService childAlertService) {
        this.childAlertService = childAlertService;
    }

    @GetMapping
    public ResponseEntity<?> getChildrenAtAddress(@RequestParam String address) {
        logger.debug("Requête reçue pour récupérer les enfants à l'adresse.");
        try {
            ChildrenAtAddressDTO result = childAlertService.getChildrenAtAddress(address);
            if (result.getChildren().isEmpty()) {
                logger.info("Aucun enfant trouvé à l'adresse.");
                return ResponseEntity.noContent().build();
            }
            logger.info("Enfants trouvés avec succès à l'adresse.");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur serveur lors de la récupération des enfants à l'adresse.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
