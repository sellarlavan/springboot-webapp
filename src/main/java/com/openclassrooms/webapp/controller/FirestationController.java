package com.openclassrooms.webapp.controller;

import com.openclassrooms.webapp.dto.FirestationCoverage;
import com.openclassrooms.webapp.model.Firestation;
import com.openclassrooms.webapp.service.interfaces.FirestationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/firestation")
public class FirestationController {
    private final FirestationService firestationService;
    private static final Logger logger = LogManager.getLogger(FirestationController.class);

    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    @PostMapping
    public ResponseEntity<Firestation> createFirestation(@RequestBody Firestation firestation) {
        logger.debug("Requête reçue pour la création d'une caserne.");
        try {
            Firestation created = firestationService.createFirestation(firestation);
            logger.info("Création de la caserne avec succès.");
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            logger.error("Erreur serveur pendant la création de la caserne.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping
    public ResponseEntity<Firestation> updateFirestation(@RequestBody Firestation updatedFirestation) {
        logger.debug("Requête reçue pour la mise à jour d'une caserne.");
        try {
            Firestation updated = firestationService.updateFirestation(updatedFirestation);
            if (updated == null) {
                return ResponseEntity.notFound().build();
            }
            logger.info("Mise à jour de la caserne avec succès.");
            return ResponseEntity.ok(updated);
        }
        catch (NoSuchElementException e){
            logger.error("Caserne non trouvée pour mise à jour.");
            return ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            logger.error("Erreur serveur pendant la mise à jour de la caserne.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFirestation(@RequestBody Firestation request) throws IOException {
        logger.debug("Requête reçue pour la suppression d'une caserne.");

        // Validation simple : soit adresse, soit station doit être renseignée
        if ((request.getAddress() == null || request.getAddress().isEmpty()) && request.getStation() == 0) {
            logger.error("Requête invalide ni adresse ni numéro de station fourni.");
            return ResponseEntity.badRequest().build();
        }

        boolean deleted;

        if (request.getAddress() != null && !request.getAddress().isEmpty()) {
            logger.debug("Suppression d'une caserne par adresse : {}", request.getAddress());
            deleted = firestationService.deleteByAddress(request.getAddress());
        } else {
            logger.debug("Suppression d'une caserne par numéro de station : {}", request.getStation());
            deleted = firestationService.deleteByStationNumber(request.getStation());
        }

        if (deleted) {
            logger.info("Suppression de la caserne réussie.");
            return ResponseEntity.noContent().build();
        } else {
            logger.error("Caserne non trouvée pour suppression.");
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping
    public ResponseEntity<FirestationCoverage> getPersonsByStation(@RequestParam int stationNumber) {
        logger.debug("Requête reçue pour la récupération des personnes couvertes par la caserne.");

        try {
            FirestationCoverage response = firestationService.getPersonsCoveredByStation(stationNumber);

            if (response == null) {
                logger.error("La caserne n'existe pas.");
                return ResponseEntity.notFound().build();
            }

            if (response.getPersons() == null || response.getPersons().isEmpty()) {
                logger.warn("La caserne ne couvre aucun résident.");
                return ResponseEntity.ok(response); // On peut aussi faire `noContent()` si tu préfères.
            }

            logger.info("Récupération réussie des personnes couvertes par la caserne");
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            logger.error("Erreur serveur lors de la récupération des personnes pour la caserne");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
