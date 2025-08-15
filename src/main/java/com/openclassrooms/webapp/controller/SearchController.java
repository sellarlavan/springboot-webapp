package com.openclassrooms.webapp.controller;

import com.openclassrooms.webapp.dto.*;
import com.openclassrooms.webapp.service.interfaces.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;

@RestController
public class SearchController {
    private final ChildAlertService childAlertService;
    private final PhoneAlertService phoneAlertService;
    private final FireService fireService;
    private final FloodService floodService;
    private final PersonService personService;
    private final CommunityService communityService;

    private static final Logger logger = LogManager.getLogger(SearchController.class);

    public SearchController(ChildAlertService childAlertService,
                            PhoneAlertService phoneAlertService,
                            FireService fireService,
                            FloodService floodService,
                            PersonService personService,
                            CommunityService communityService){
        this.childAlertService = childAlertService;
        this.phoneAlertService = phoneAlertService;
        this.fireService = fireService;
        this.floodService = floodService;
        this.personService = personService;
        this.communityService = communityService;
    }

    @GetMapping("/childAlert")
    public ResponseEntity<ChildrenAtAddressDTO> getChildrenAtAddress(@RequestParam String address) {
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

    @GetMapping("/phoneAlert")
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

    @GetMapping("/fire")
    public ResponseEntity<FireDTO> getFireInfo(@RequestParam String address) {
        logger.debug("Requête reçue pour les infos incendie à l'adresse.");

        try {
            FireDTO result = fireService.getResidentsAndStation(address);

            if (result == null) {
                logger.error("Adresse inexistante.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            logger.info("Résidents trouvés à l'adresse.");
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            logger.error("Erreur serveur lors de la récupération des infos incendie pour l'adresse.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/flood/stations")
    public ResponseEntity<FloodStationDTO> getFloodStations(@RequestParam List<Integer> stations) {
        logger.debug("Requête reçue pour les foyers couverts par les stations.");

        try {
            FloodStationDTO result = floodService.getHouseholdsByStations(stations);

            logger.info("Foyers trouvés pour les stations.");
            return ResponseEntity.ok(result);

        } catch (IOException e) {
            logger.error("Erreur lors de la récupération des foyers pour les stations.");
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/personInfolastName")
    public ResponseEntity<List<PersonInfoByLastNameDTO>> getPersonInfo(@RequestParam String lastName) {
        logger.debug("Requête reçue pour la récupération d'une personne avec son nom.");
        try {
            List<PersonInfoByLastNameDTO> result = personService.getPersonInfoByLastName(lastName);

            logger.info("Personne récupérée avec succès.");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur serveur pendant la récupération d'une personne.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/communityEmail")
    public ResponseEntity<CommunityEmailDTO> getCommunityEmail(@RequestParam String city) {
        logger.debug("Requête reçue pour récupérer les emails de la ville.");
        try {
            CommunityEmailDTO result = communityService.getEmailsByCity(city);
            if (result.getEmails().isEmpty()) {
                logger.error("Aucune adresse e-mail trouvée pour la ville.");
                return ResponseEntity.ok().build();
            }
            logger.info("Emails récupérés avec succès pour la ville.");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur serveur lors de la récupération des emails pour la ville.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
