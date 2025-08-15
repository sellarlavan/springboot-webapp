package com.openclassrooms.webapp.service.impl;

import com.openclassrooms.webapp.model.Firestation;
import com.openclassrooms.webapp.model.Person;
import com.openclassrooms.webapp.repository.FirestationRepository;
import com.openclassrooms.webapp.repository.PersonRepository;
import com.openclassrooms.webapp.service.interfaces.PhoneAlertService;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhoneAlertServiceImpl implements PhoneAlertService {

    private final FirestationRepository firestationRepository;
    private final PersonRepository personRepository;

    public PhoneAlertServiceImpl(FirestationRepository firestationRepository, PersonRepository personRepository) {
        this.firestationRepository = firestationRepository;
        this.personRepository = personRepository;
    }

    @Override
    public List<String> getPhoneNumbersByStation(int stationNumber) throws IOException {
        List<Firestation> firestations = firestationRepository.findAll();

        List<String> addressesCovered = firestations.stream()
                .filter(fs -> fs.getStation() == stationNumber)
                .map(Firestation::getAddress)
                .toList();

        if (addressesCovered.isEmpty()) {
            return Collections.emptyList();
        }

        List<Person> persons = personRepository.findAll();

        List<String> phoneNumbers = persons.stream()
                .filter(p -> addressesCovered.contains(p.getAddress()))
                .map(Person::getPhone)
                .distinct()
                .collect(Collectors.toList());
        return phoneNumbers;
    }

}
