package com.openclassrooms.webapp.controller;

import com.openclassrooms.webapp.service.PhoneAlertService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/phoneAlert")
public class PhoneAlertController {
    private final PhoneAlertService phoneAlertService;

    public PhoneAlertController(PhoneAlertService phoneAlertService) {
        this.phoneAlertService = phoneAlertService;
    }

    @GetMapping
    public ResponseEntity<List<String>> getPhoneNumbersByStation(@RequestParam int firestation) {
        try {
            List<String> phoneNumbers = phoneAlertService.getPhoneNumbersByStation(firestation);
            return ResponseEntity.ok(phoneNumbers);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
