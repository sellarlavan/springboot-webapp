package com.openclassrooms.webapp.controller;

import com.openclassrooms.webapp.model.Person;
import com.openclassrooms.webapp.service.interfaces.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.NoSuchElementException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;
    private static final Logger logger = LogManager.getLogger(PersonController.class);

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        logger.debug("Requête reçue pour la création d'une personne.");
        try {
            Person createdPerson = personService.createPerson(person);
            logger.info("Création d'une personne avec succès.");
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPerson);
        } catch (Exception e) {
            logger.error("Erreur lors de la création d'une personne. ");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{firstName}/{lastName}")
    public ResponseEntity<Person> updatePerson(@PathVariable String firstName, @PathVariable String lastName, @RequestBody Person updatedData) {
        logger.debug("Requête reçue pour la mise à jour d'une personne.");
        try {
            Person updated = personService.updatePerson(firstName, lastName, updatedData);
            logger.info("Mise à jour de la personne avec succès.");
            return ResponseEntity.ok(updated);
        } catch (NoSuchElementException e) {
            logger.error("Personne non trouvée pour la mise à jour.");
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erreur serveur pendant la mise à jour de la personne.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<Void> deletePerson(@PathVariable String firstName, @PathVariable String lastName) throws IOException {
        logger.debug("Requête reçu pour la suppression d'une personne.");
        boolean deleted = personService.deletePerson(firstName, lastName);
        if (deleted) {
            logger.info("Personne supprimée avec succès.");
            return ResponseEntity.noContent().build();
        } else {
            logger.info("Personne non trouvée pour la suppression.");
            return ResponseEntity.notFound().build();
        }
    }
}
