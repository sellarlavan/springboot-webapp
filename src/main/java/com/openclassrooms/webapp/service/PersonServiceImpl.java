package com.openclassrooms.webapp.service;

import com.openclassrooms.webapp.dto.PersonInfoByLastNameDTO;
import com.openclassrooms.webapp.model.MedicalRecord;
import com.openclassrooms.webapp.model.Person;

import com.openclassrooms.webapp.repository.MedicalRecordRepository;
import com.openclassrooms.webapp.repository.PersonRepository;
import com.openclassrooms.webapp.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.IOException;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public PersonServiceImpl(PersonRepository personRepository, MedicalRecordRepository medicalRecordRepository) {
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @Override
    public Person createPerson(Person person) throws IOException {
        List<Person> persons = personRepository.findAll();
        persons.add(person);
        personRepository.saveAll(persons);
        return person;
    }

    @Override
    public Person updatePerson(String firstName, String lastName, Person updatedPerson) throws IOException {
        List<Person> persons = personRepository.findAll();
        for (int i = 0; i < persons.size(); i++) {
            Person p = persons.get(i);
            if (p.getFirstName().equalsIgnoreCase(firstName) && p.getLastName().equalsIgnoreCase(lastName)) {
                updatedPerson.setFirstName(firstName);
                updatedPerson.setLastName(lastName);
                persons.set(i, updatedPerson);
                personRepository.saveAll(persons);
                return updatedPerson;
            }
        }
        return null;
    }

    @Override
    public boolean deletePerson(String firstName, String lastName) throws IOException {
        List<Person> persons = personRepository.findAll();
        boolean removed = persons.removeIf(p ->
                p.getFirstName().equalsIgnoreCase(firstName)
                        && p.getLastName().equalsIgnoreCase(lastName));
        if (removed) {
            personRepository.saveAll(persons);
        }
        return removed;
    }

    public List<PersonInfoByLastNameDTO> getPersonInfoByLastName(String lastName) throws IOException {
        List<Person> persons = personRepository.findAll();
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findAll();

        List<PersonInfoByLastNameDTO> result = new ArrayList<>();

        for (Person person : persons) {
            if (person.getLastName().equalsIgnoreCase(lastName)) {
                Optional<MedicalRecord> medicalOpt = medicalRecords.stream()
                        .filter(mr -> mr.getFirstName().equalsIgnoreCase(person.getFirstName())
                                && mr.getLastName().equalsIgnoreCase(person.getLastName()))
                        .findFirst();


                int age = medicalOpt.map(mr -> DateUtils.calculateAge(mr.getBirthdate())).orElse(0);
                List<String> medications = medicalOpt.map(MedicalRecord::getMedications).orElse(Collections.emptyList());
                List<String> allergies = medicalOpt.map(MedicalRecord::getAllergies).orElse(Collections.emptyList());

                result.add(new PersonInfoByLastNameDTO(
                        person.getFirstName(),
                        person.getLastName(),
                        person.getAddress(),
                        age,
                        person.getEmail(),
                        medications,
                        allergies
                ));
            }
        }

        return result;
    }
}