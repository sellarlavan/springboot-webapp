package com.openclassrooms.webapp.service;

import com.openclassrooms.webapp.dto.CommunityEmailDTO;
import com.openclassrooms.webapp.model.Person;
import com.openclassrooms.webapp.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommunityService {

    private final PersonRepository personRepository;

    public CommunityService(PersonRepository personRepository){
        this.personRepository = personRepository;
    }

    public CommunityEmailDTO getEmailsByCity(String city) throws IOException {
        List<Person> persons = personRepository.findAll();

        List<String> emails = persons.stream()
                .filter(p -> p.getCity().equalsIgnoreCase(city))
                .map(Person::getEmail)
                .distinct()
                .collect(Collectors.toList());

        return new CommunityEmailDTO(emails);
    }

}
