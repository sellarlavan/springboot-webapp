package com.openclassrooms.webapp.service.impl;

import com.openclassrooms.webapp.dto.FireDTO;
import com.openclassrooms.webapp.dto.PersonInfoDTO;
import com.openclassrooms.webapp.model.Firestation;
import com.openclassrooms.webapp.model.MedicalRecord;
import com.openclassrooms.webapp.model.Person;
import com.openclassrooms.webapp.repository.FirestationRepository;
import com.openclassrooms.webapp.repository.MedicalRecordRepository;
import com.openclassrooms.webapp.repository.PersonRepository;
import com.openclassrooms.webapp.service.interfaces.FireService;
import com.openclassrooms.webapp.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FireServiceImpl implements FireService {

    private final FirestationRepository firestationRepository;
    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public FireServiceImpl(FirestationRepository firestationRepository,
                           PersonRepository personRepository,
                           MedicalRecordRepository medicalRecordRepository) {
        this.firestationRepository = firestationRepository;
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @Override
    public FireDTO getResidentsAndStation(String address) throws IOException {
        List<Person> persons = personRepository.findAll();
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findAll();
        List<Firestation> firestations = firestationRepository.findAll();

        boolean addressExistsInPersons = persons.stream()
                .anyMatch(p -> p.getAddress().equalsIgnoreCase(address));

        boolean addressExistsInStations = firestations.stream()
                .anyMatch(f -> f.getAddress().equalsIgnoreCase(address));

        if (!addressExistsInPersons && !addressExistsInStations) {
            return null;
        }

        Optional<Firestation> stationOpt = firestations.stream()
                .filter(f -> f.getAddress().equalsIgnoreCase(address))
                .findFirst();

        Integer stationNumber = stationOpt.map(Firestation::getStation).orElse(null);

        List<PersonInfoDTO> residents = persons.stream()
                .filter(p -> p.getAddress().equalsIgnoreCase(address))
                .map(p -> {
                    Optional<MedicalRecord> medicalOpt = medicalRecords.stream()
                            .filter(mr -> mr.getFirstName().equalsIgnoreCase(p.getFirstName()) &&
                                    mr.getLastName().equalsIgnoreCase(p.getLastName()))
                            .findFirst();

                    int age = medicalOpt.map(mr -> DateUtils.calculateAge(mr.getBirthdate())).orElse(0);
                    List<String> medications = medicalOpt.map(MedicalRecord::getMedications).orElse(Collections.emptyList());
                    List<String> allergies = medicalOpt.map(MedicalRecord::getAllergies).orElse(Collections.emptyList());

                    return new PersonInfoDTO(
                            p.getFirstName(),
                            p.getLastName(),
                            p.getPhone(),
                            age,
                            medications,
                            allergies
                    );
                })
                .collect(Collectors.toList());

        return new FireDTO(residents, stationNumber);
    }





}
