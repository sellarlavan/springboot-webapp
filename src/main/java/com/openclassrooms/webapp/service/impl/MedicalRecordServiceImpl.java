package com.openclassrooms.webapp.service.impl;

import com.openclassrooms.webapp.model.MedicalRecord;
import com.openclassrooms.webapp.repository.MedicalRecordRepository;
import com.openclassrooms.webapp.service.interfaces.MedicalRecordService;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordServiceImpl(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @Override
    public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) throws IOException {
        List<MedicalRecord> records = medicalRecordRepository.findAll();
        records.add(medicalRecord);
        medicalRecordRepository.saveAll(records);
        return medicalRecord;
    }

    @Override
    public MedicalRecord updateMedicalRecord(String firstName, String lastName, MedicalRecord updatedRecord) throws IOException {
        List<MedicalRecord> records = medicalRecordRepository.findAll();

        for (int i = 0; i < records.size(); i++) {
            MedicalRecord mr = records.get(i);

            if (mr.getFirstName() != null && mr.getLastName() != null &&
                    mr.getFirstName().equalsIgnoreCase(firstName) &&
                    mr.getLastName().equalsIgnoreCase(lastName)) {

                updatedRecord.setFirstName(firstName);
                updatedRecord.setLastName(lastName);
                records.set(i, updatedRecord);
                medicalRecordRepository.saveAll(records);
                return updatedRecord;
            }
        }
        return null;
    }

    @Override
    public boolean deleteMedicalRecord(String firstName, String lastName) throws IOException {
        List<MedicalRecord> records = medicalRecordRepository.findAll();

        boolean removed = records.removeIf(mr ->
                mr.getFirstName() != null &&
                        mr.getLastName() != null &&
                        mr.getFirstName().equalsIgnoreCase(firstName) &&
                        mr.getLastName().equalsIgnoreCase(lastName)
        );

        if (removed) {
            medicalRecordRepository.saveAll(records);
        }
        return removed;
    }
}
