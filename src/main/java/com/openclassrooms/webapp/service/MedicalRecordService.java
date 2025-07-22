package com.openclassrooms.webapp.service;

import com.openclassrooms.webapp.model.MedicalRecord;

import java.io.IOException;
import java.util.List;

public interface MedicalRecordService {
    MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) throws IOException;

    MedicalRecord updateMedicalRecord(String firstName, String lastName, MedicalRecord updatedRecord) throws IOException;

    boolean deleteMedicalRecord(String firstName, String lastName) throws IOException;

}
