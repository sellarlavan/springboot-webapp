package com.openclassrooms.webapp.service;

import com.openclassrooms.webapp.dto.FloodStationDTO;
import com.openclassrooms.webapp.dto.ResidentInfoDTO;
import com.openclassrooms.webapp.model.Firestation;
import com.openclassrooms.webapp.model.MedicalRecord;
import com.openclassrooms.webapp.model.Person;
import com.openclassrooms.webapp.repository.FirestationRepository;
import com.openclassrooms.webapp.repository.MedicalRecordRepository;
import com.openclassrooms.webapp.repository.PersonRepository;
import com.openclassrooms.webapp.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FloodService {

    private final FirestationRepository firestationRepository;
    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public FloodService(FirestationRepository firestationRepository, PersonRepository personRepository,
                        MedicalRecordRepository medicalRecordRepository) {
        this.firestationRepository = firestationRepository;
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public FloodStationDTO getHouseholdsByStations(List<Integer> stationNumbers) throws IOException {
        List<Firestation> allStations = firestationRepository.findAll();
        List<Person> allPersons = personRepository.findAll();
        List<MedicalRecord> allRecords = medicalRecordRepository.findAll();

        // Obtenir toutes les adresses couvertes par ces stations
        Set<String> addresses = allStations.stream()
                .filter(f -> stationNumbers.contains(f.getStation()))
                .map(Firestation::getAddress)
                .collect(Collectors.toSet());

        Map<String, List<ResidentInfoDTO>> households = new HashMap<>();

        for (String address : addresses) {
            List<ResidentInfoDTO> residentsAtAddress = allPersons.stream()
                    .filter(p -> address.equalsIgnoreCase(p.getAddress()))
                    .map(p -> {
                        Optional<MedicalRecord> medicalOpt = allRecords.stream()
                                .filter(m -> m.getFirstName().equalsIgnoreCase(p.getFirstName()) &&
                                        m.getLastName().equalsIgnoreCase(p.getLastName()))
                                .findFirst();

                        int age = medicalOpt.map(mr -> DateUtils.calculateAge(mr.getBirthdate())).orElse(0);

                        List<String> meds = medicalOpt.map(MedicalRecord::getMedications).orElse(Collections.emptyList());
                        List<String> allergies = medicalOpt.map(MedicalRecord::getAllergies).orElse(Collections.emptyList());

                        return new ResidentInfoDTO(
                                p.getFirstName(),
                                p.getLastName(),
                                p.getPhone(),
                                age,
                                meds,
                                allergies
                        );
                    })
                    .collect(Collectors.toList());

            households.put(address, residentsAtAddress);
        }

        return new FloodStationDTO(households);
    }
}