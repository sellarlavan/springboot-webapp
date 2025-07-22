package com.openclassrooms.webapp.controller;

import com.openclassrooms.webapp.dto.FloodStationDTO;
import com.openclassrooms.webapp.service.FloodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/flood")
public class FloodController {

    private final FloodService floodService;

    public FloodController(FloodService floodService) {
        this.floodService = floodService;
    }

    @GetMapping("/stations")
    public ResponseEntity<?> getFloodStations(@RequestParam List<Integer> stations) {
        try {
            FloodStationDTO result = floodService.getHouseholdsByStations(stations);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erreur lors du chargement des données.");
        }
    }
}