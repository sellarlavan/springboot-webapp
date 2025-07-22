package com.openclassrooms.webapp.service;

import com.openclassrooms.webapp.dto.ChildAlert;
import com.openclassrooms.webapp.dto.ChildrenAtAddressDTO;
import com.openclassrooms.webapp.dto.PersonDTO;
import com.openclassrooms.webapp.model.MedicalRecord;
import com.openclassrooms.webapp.model.Person;
import com.openclassrooms.webapp.repository.MedicalRecordRepository;
import com.openclassrooms.webapp.repository.PersonRepository;
import com.openclassrooms.webapp.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChildAlertService {
    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public ChildAlertService(PersonRepository personRepository, MedicalRecordRepository medicalRecordRepository) {
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public ChildrenAtAddressDTO getChildrenAtAddress(String address) throws IOException {
        List<Person> persons = personRepository.findAll();
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findAll();

        // Filtrer les personnes vivant à cette adresse
        List<Person> residents = persons.stream()
                .filter(p -> p.getAddress().equalsIgnoreCase(address))
                .collect(Collectors.toList());

        List<ChildAlert> children = new ArrayList<>();
        List<PersonDTO> others = new ArrayList<>();

        for (Person resident : residents) {
            // Trouver le dossier médical
            Optional<MedicalRecord> medicalRecordOpt = medicalRecords.stream()
                    .filter(mr -> mr.getFirstName().equalsIgnoreCase(resident.getFirstName()) &&
                            mr.getLastName().equalsIgnoreCase(resident.getLastName()))
                    .findFirst();

            int age = medicalRecordOpt.map(mr -> DateUtils.calculateAge(mr.getBirthdate())).orElse(0);

            if (age <= 18) {
                // C’est un enfant
                children.add(new ChildAlert(resident.getFirstName(), resident.getLastName(), age));
            } else {
                // C’est un adulte
                others.add(new PersonDTO(resident.getFirstName(), resident.getLastName()));
            }
        }

        // S’il n’y a pas d’enfant, on peut renvoyer une réponse vide (null ou objet vide)
        if (children.isEmpty()) {
            return new ChildrenAtAddressDTO(Collections.emptyList(), Collections.emptyList());
        }


        return new ChildrenAtAddressDTO(children, others);
    }


}
