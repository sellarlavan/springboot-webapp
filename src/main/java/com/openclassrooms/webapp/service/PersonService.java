package com.openclassrooms.webapp.service;

import com.openclassrooms.webapp.dto.PersonInfoByLastNameDTO;
import com.openclassrooms.webapp.model.Person;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

public interface PersonService {
    Person createPerson(Person person) throws IOException;

    Person updatePerson(String firstName, String lastName, Person updatedPerson) throws IOException;

    boolean deletePerson(String firstName, String lastName) throws IOException;

    List<PersonInfoByLastNameDTO> getPersonInfoByLastName(String lastName) throws IOException;
}
