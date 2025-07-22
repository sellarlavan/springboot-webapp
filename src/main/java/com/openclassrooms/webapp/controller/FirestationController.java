package com.openclassrooms.webapp.controller;

import com.openclassrooms.webapp.dto.FirestationCoverage;
import com.openclassrooms.webapp.model.Firestation;
import com.openclassrooms.webapp.service.FirestationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
@RequestMapping("/firestation")
public class FirestationController {
    private final FirestationService firestationService;

    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    @PostMapping
    public ResponseEntity<Firestation> createFirestation(@RequestBody Firestation firestation) {
        try {
            Firestation created = firestationService.createFirestation(firestation);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping
    public ResponseEntity<Firestation> updateFirestation(@RequestBody Firestation updatedFirestation) {
        try {
            Firestation updated = firestationService.updateFirestation(updatedFirestation);
            if (updated == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMapping(@RequestBody Firestation request) throws IOException {
        if (request.getAddress() == null && request.getStation() == 0) {
            return ResponseEntity.badRequest().build();
        }

        boolean deleted;

        if (request.getAddress() != null) {
            deleted = firestationService.deleteByAddress(request.getAddress());
        } else {
            deleted = firestationService.deleteByStationNumber(request.getStation());
        }

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<FirestationCoverage> getPersonsByStation(@RequestParam int stationNumber) {
        try {
            FirestationCoverage response = firestationService.getPersonsCoveredByStation(stationNumber);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
