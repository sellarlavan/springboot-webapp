package com.openclassrooms.webapp.service;

import com.openclassrooms.webapp.model.Firestation;
import com.openclassrooms.webapp.model.Person;
import com.openclassrooms.webapp.repository.FirestationRepository;
import com.openclassrooms.webapp.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhoneAlertService {

    private final FirestationRepository firestationRepository;
    private final PersonRepository personRepository;

    public PhoneAlertService(FirestationRepository firestationRepository, PersonRepository personRepository) {
        this.firestationRepository = firestationRepository;
        this.personRepository = personRepository;
    }

    public List<String> getPhoneNumbersByStation(int stationNumber) throws IOException {
        List<Firestation> firestations = firestationRepository.findAll();

        // Récupérer les adresses couvertes par la station
        List<String> addressesCovered = firestations.stream()
                .filter(fs -> fs.getStation() == stationNumber)
                .map(Firestation::getAddress)
                .collect(Collectors.toList());

        // Récupérer toutes les personnes
        List<Person> persons = personRepository.findAll();

        // Filtrer par adresse, récupérer les téléphones, supprimer doublons
        List<String> phoneNumbers = persons.stream()
                .filter(p -> addressesCovered.contains(p.getAddress()))
                .map(Person::getPhone)
                .distinct()
                .collect(Collectors.toList());

        return phoneNumbers;
    }

}
