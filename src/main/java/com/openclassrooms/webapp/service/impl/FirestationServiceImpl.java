package com.openclassrooms.webapp.service.impl;

import com.openclassrooms.webapp.dto.FirestationCoverage;
import com.openclassrooms.webapp.dto.PersonCoveredByStationDTO;
import com.openclassrooms.webapp.model.Firestation;
import com.openclassrooms.webapp.model.MedicalRecord;
import com.openclassrooms.webapp.model.Person;
import com.openclassrooms.webapp.repository.FirestationRepository;
import com.openclassrooms.webapp.repository.MedicalRecordRepository;
import com.openclassrooms.webapp.repository.PersonRepository;
import com.openclassrooms.webapp.service.interfaces.FirestationService;
import com.openclassrooms.webapp.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@Service
public class FirestationServiceImpl implements FirestationService {
    private final FirestationRepository firestationRepository;
    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public FirestationServiceImpl(FirestationRepository firestationRepository, PersonRepository personRepository, MedicalRecordRepository medicalRecordRepository
    ) {
        this.firestationRepository = firestationRepository;
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @Override
    public Firestation createFirestation(Firestation firestation) throws IOException {
        List<Firestation> firestations = firestationRepository.findAll();
        firestations.add(firestation);
        firestationRepository.saveAll(firestations);
        return firestation;
    }

    @Override
    public Firestation updateFirestation(Firestation updatedFirestation) throws IOException {
        List<Firestation> firestations = firestationRepository.findAll();

        for (int i = 0; i < firestations.size(); i++) {
            Firestation fs = firestations.get(i);


            if (fs.getAddress().equalsIgnoreCase(updatedFirestation.getAddress())) {

                firestations.set(i, updatedFirestation);
                firestationRepository.saveAll(firestations);
                return updatedFirestation;
            }
        }
        throw new NoSuchElementException("Adresse inexistante.");
    }

    @Override
    public boolean deleteByAddress(String address) throws IOException {
        List<Firestation> firestations = firestationRepository.findAll();
        boolean removed = firestations.removeIf(f -> f.getAddress().equalsIgnoreCase(address));
        if (removed) {
            firestationRepository.saveAll(firestations);
        }
        return removed;
    }

    @Override
    public boolean deleteByStationNumber(int stationNumber) throws IOException {
        List<Firestation> firestations = firestationRepository.findAll();
        boolean removed = firestations.removeIf(f -> f.getStation() == stationNumber);
        if (removed) {
            firestationRepository.saveAll(firestations);
        }
        return removed;
    }

    public FirestationCoverage getPersonsCoveredByStation(int stationNumber) throws IOException {
        List<Firestation> firestations = firestationRepository.findAll();

        List<String> addresses = firestations.stream()
                .filter(f -> f.getStation() == stationNumber)
                .map(Firestation::getAddress)
                .toList();

        if (addresses.isEmpty()) {
            return null;
        }

        List<Person> persons = personRepository.findAll();
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findAll();

        List<PersonCoveredByStationDTO> coveredPersons = new ArrayList<>();
        int adultCount = 0;
        int childCount = 0;

        for (Person person : persons) {
            if (addresses.contains(person.getAddress())) {
                int age = -1;

                for (MedicalRecord mr : medicalRecords) {
                    if (mr.getFirstName().equalsIgnoreCase(person.getFirstName())
                            && mr.getLastName().equalsIgnoreCase(person.getLastName())) {
                        age = DateUtils.calculateAge(mr.getBirthdate());
                        break;
                    }
                }

                if (age != -1) {
                    if (age <= 18) {
                        childCount++;
                    } else {
                        adultCount++;
                    }
                }

                coveredPersons.add(new PersonCoveredByStationDTO(
                        person.getFirstName(),
                        person.getLastName(),
                        person.getAddress(),
                        person.getPhone()
                ));
            }
        }
        return new FirestationCoverage(coveredPersons, adultCount, childCount);
    }
}