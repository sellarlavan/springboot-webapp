package com.openclassrooms.webapp.service.impl;

import com.openclassrooms.webapp.dto.CommunityEmailDTO;
import com.openclassrooms.webapp.model.Person;
import com.openclassrooms.webapp.repository.PersonRepository;
import com.openclassrooms.webapp.service.interfaces.CommunityService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CommunityServiceImpl implements CommunityService {

    private final PersonRepository personRepository;

    public CommunityServiceImpl(PersonRepository personRepository){
        this.personRepository = personRepository;
    }

    @Override
    public CommunityEmailDTO getEmailsByCity(String city) throws IOException {
        List<Person> persons = personRepository.findAll();

        List<String> emails = persons.stream()
                .filter(p -> p.getCity() != null && p.getCity().equalsIgnoreCase(city))
                .map(Person::getEmail)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        return new CommunityEmailDTO(emails);
    }


}
