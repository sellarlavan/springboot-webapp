package com.openclassrooms.webapp.repository;

import com.openclassrooms.webapp.model.DataContainer;
import com.openclassrooms.webapp.model.MedicalRecord;
import org.springframework.stereotype.Repository;
import java.io.IOException;
import java.util.List;

@Repository
public class MedicalRecordRepository {
    private final DataRepository dataRepository;

    public MedicalRecordRepository(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public List<MedicalRecord> findAll() throws IOException {
        return dataRepository.loadData().getMedicalrecords();
    }

    public void saveAll(List<MedicalRecord> medicalRecords) throws IOException {
        DataContainer data = dataRepository.loadData();
        data.setMedicalrecords(medicalRecords);
        dataRepository.saveData(data);
    }
}
