package com.openclassrooms.webapp.controller;

import com.openclassrooms.webapp.dto.ChildrenAtAddressDTO;
import com.openclassrooms.webapp.service.ChildAlertService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/childAlert")
public class ChildAlertController {
    private final ChildAlertService childAlertService;

    public ChildAlertController(ChildAlertService childAlertService) {
        this.childAlertService = childAlertService;
    }

    @GetMapping
    public ResponseEntity<?> getChildrenAtAddress(@RequestParam String address) {
        try {
            ChildrenAtAddressDTO result = childAlertService.getChildrenAtAddress(address);
            if (result.getChildren().isEmpty()) {
                // Pas d'enfant à cette adresse, on peut renvoyer une chaîne vide ou un 204 No Content
                return ResponseEntity.ok("");
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur serveur");
        }
    }
}
