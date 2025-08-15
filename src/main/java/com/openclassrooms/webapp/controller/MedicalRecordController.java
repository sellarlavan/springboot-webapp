package com.openclassrooms.webapp.controller;

import com.openclassrooms.webapp.model.MedicalRecord;
import com.openclassrooms.webapp.service.impl.MedicalRecordServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    private final MedicalRecordServiceImpl medicalRecordService;
    private static final Logger logger = LogManager.getLogger(MedicalRecordController.class);

    public MedicalRecordController(MedicalRecordServiceImpl medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping
    public ResponseEntity<MedicalRecord> createMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        logger.info("Requête reçue pour la création d'un dossier médical.");
        try {
            MedicalRecord created = medicalRecordService.createMedicalRecord(medicalRecord);
            logger.info("Dossier médical crée avec succès.");
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            logger.error("Erreur serveur pendant la création du dossier médical.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{firstName}/{lastName}")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(
            @PathVariable String firstName,
            @PathVariable String lastName,
            @RequestBody MedicalRecord updatedRecord) {

        logger.info("Requête reçue pour la mise à jour du dossier médical.");

        try {
            MedicalRecord updated = medicalRecordService.updateMedicalRecord(firstName, lastName, updatedRecord);

            if (updated == null) {
                logger.info("Dossier médical non trouvé.");
                return ResponseEntity.notFound().build();
            }

            logger.info("Dossier médical mis à jour avec succès.}");
            return ResponseEntity.ok(updated);

        } catch (Exception e) {
            logger.error("Erreur serveur pendant la mise à jour du dossier médical.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<Void> deleteMedicalRecord(
            @PathVariable String firstName,
            @PathVariable String lastName) {

        logger.info("Requête reçue pour la suppression du dossier médical.");

        try {
            boolean deleted = medicalRecordService.deleteMedicalRecord(firstName, lastName);

            if (deleted) {
                logger.info("Dossier médical supprimé avec succès.");
                return ResponseEntity.noContent().build();
            } else {
                logger.error("Dossier médical non trouvé pour suppression.");
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            logger.error("Erreur serveur pendant la suppression du dossier médical.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
