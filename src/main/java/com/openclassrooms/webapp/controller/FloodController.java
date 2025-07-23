package com.openclassrooms.webapp.controller;

import com.openclassrooms.webapp.dto.FloodStationDTO;
import com.openclassrooms.webapp.service.interfaces.FloodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@RestController
@RequestMapping("/flood")
public class FloodController {

    private final FloodService floodService;
    private static final Logger logger = LogManager.getLogger(FloodController.class);

    public FloodController(FloodService floodService) {
        this.floodService = floodService;
    }

    @GetMapping("/stations")
    public ResponseEntity<?> getFloodStations(@RequestParam List<Integer> stations) {
        logger.debug("Requête reçue pour les foyers couverts par les stations.");

        try {
            FloodStationDTO result = floodService.getHouseholdsByStations(stations);

            if (result.getHouseholds().isEmpty()) {
                logger.error("Aucun foyer trouvé pour les stations.");
                return ResponseEntity.ok().build();
            }

            logger.info("Foyers trouvés pour les stations.");
            return ResponseEntity.ok(result);

        } catch (IOException e) {
            logger.error("Erreur lors de la récupération des foyers pour les stations.");
            return ResponseEntity.internalServerError().build();
        }
    }

}