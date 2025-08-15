package com.openclassrooms.webapp.service.impl;

import com.openclassrooms.webapp.dto.ChildAlert;
import com.openclassrooms.webapp.dto.ChildrenAtAddressDTO;
import com.openclassrooms.webapp.dto.PersonDTO;
import com.openclassrooms.webapp.model.MedicalRecord;
import com.openclassrooms.webapp.model.Person;
import com.openclassrooms.webapp.repository.MedicalRecordRepository;
import com.openclassrooms.webapp.repository.PersonRepository;
import com.openclassrooms.webapp.service.interfaces.ChildAlertService;
import com.openclassrooms.webapp.utils.DateUtils;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChildAlertServiceImpl implements ChildAlertService {
    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public ChildAlertServiceImpl(PersonRepository personRepository, MedicalRecordRepository medicalRecordRepository) {
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @Override
    public ChildrenAtAddressDTO getChildrenAtAddress(String address) throws IOException {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("L'adresse ne peut pas être vide");
        }

        List<Person> persons = personRepository.findAll();
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findAll();
        List<Person> residents = persons.stream()
                .filter(p -> p.getAddress() != null && p.getAddress().equalsIgnoreCase(address))
                .toList();
        List<ChildAlert> children = new ArrayList<>();
        List<PersonDTO> others = new ArrayList<>();

        for (Person resident : residents) {
            Optional<MedicalRecord> medicalRecordOpt = medicalRecords.stream()
                    .filter(mr -> mr.getFirstName() != null && mr.getLastName() != null &&
                            mr.getFirstName().equalsIgnoreCase(resident.getFirstName()) &&
                            mr.getLastName().equalsIgnoreCase(resident.getLastName()))
                    .findFirst();

            int age = medicalRecordOpt.map(mr -> DateUtils.calculateAge(mr.getBirthdate())).orElse(-1);

            if (age >= 0 && age <= 18) {
                children.add(new ChildAlert(resident.getFirstName(), resident.getLastName(), age));
            } else if (age > 18) {
                others.add(new PersonDTO(resident.getFirstName(), resident.getLastName()));
            }
        }

        return new ChildrenAtAddressDTO(children, others);
    }
}
