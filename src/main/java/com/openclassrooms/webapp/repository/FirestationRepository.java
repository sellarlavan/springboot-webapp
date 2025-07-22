package com.openclassrooms.webapp.repository;

import com.openclassrooms.webapp.model.DataContainer;
import com.openclassrooms.webapp.model.Firestation;
import org.springframework.stereotype.Repository;
import java.io.IOException;
import java.util.List;

@Repository
public class FirestationRepository {
    private final DataRepository dataRepository;

    public FirestationRepository(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public List<Firestation> findAll() throws IOException {
        return dataRepository.loadData().getFirestations();
    }

    public void saveAll(List<Firestation> firestations) throws IOException {
        DataContainer data = dataRepository.loadData();
        data.setFirestations(firestations);
        dataRepository.saveData(data);
    }
}
