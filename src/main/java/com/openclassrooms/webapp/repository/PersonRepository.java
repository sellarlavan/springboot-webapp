package com.openclassrooms.webapp.repository;



import com.openclassrooms.webapp.model.DataContainer;
import com.openclassrooms.webapp.model.Person;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
public class PersonRepository {

    private final DataRepository dataRepository;

    public PersonRepository(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public List<Person> findAll() throws IOException {
        return dataRepository.loadData().getPersons();
    }

    public void saveAll(List<Person> persons) throws IOException {
        DataContainer data = dataRepository.loadData();
        data.setPersons(persons);
        dataRepository.saveData(data);
    }
}
