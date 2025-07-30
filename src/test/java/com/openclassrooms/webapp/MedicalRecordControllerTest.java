package com.openclassrooms.webapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.webapp.model.MedicalRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateMedicalRecordShouldReturnCreated() throws Exception {
        MedicalRecord newRecord = new MedicalRecord();
        newRecord.setFirstName("TestMed");
        newRecord.setLastName("Record");
        newRecord.setBirthdate("01/01/2000");
        newRecord.setMedications(List.of("med1:100mg", "med2:200mg"));
        newRecord.setAllergies(List.of("pollen", "gluten"));

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRecord)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("TestMed"))
                .andExpect(jsonPath("$.lastName").value("Record"))
                .andExpect(jsonPath("$.birthdate").value("01/01/2000"))
                .andExpect(jsonPath("$.medications").isArray())
                .andExpect(jsonPath("$.allergies").isArray());
    }

    @Test
    public void testUpdateExistingMedicalRecordShouldReturnUpdated() throws Exception {
        MedicalRecord updated = new MedicalRecord();
        updated.setFirstName("MedUpdateFirst");
        updated.setLastName("MedUpdateLast");
        updated.setBirthdate("01/01/2001");
        updated.setMedications(List.of("newmed:500mg"));
        updated.setAllergies(List.of("dust"));

        mockMvc.perform(put("/medicalRecord/MedUpdateFirst/MedUpdateLast")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("MedUpdateFirst"))
                .andExpect(jsonPath("$.lastName").value("MedUpdateLast"))
                .andExpect(jsonPath("$.birthdate").value("01/01/2001"))
                .andExpect(jsonPath("$.medications[0]").value("newmed:500mg"))
                .andExpect(jsonPath("$.allergies[0]").value("dust"));
    }

    @Test
    public void testUpdateNonExistingMedicalRecordShouldReturnNotFound() throws Exception {
        MedicalRecord updated = new MedicalRecord();
        updated.setFirstName("NonExisting");
        updated.setLastName("Record");
        updated.setBirthdate("01/01/2001");
        updated.setMedications(List.of("med:0mg"));
        updated.setAllergies(List.of("dust"));

        mockMvc.perform(put("/medicalRecord/NonExisting/Record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteExistingMedicalRecordShouldReturnNoContent() throws Exception {
        MedicalRecord recordToDelete = new MedicalRecord();
        recordToDelete.setFirstName("DeleteMed");
        recordToDelete.setLastName("Record");
        recordToDelete.setBirthdate("02/02/1992");
        recordToDelete.setMedications(List.of("tempmed:10mg"));
        recordToDelete.setAllergies(List.of("none"));

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recordToDelete)))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/medicalRecord/DeleteMed/Record"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteNonExistingMedicalRecordShouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/medicalRecord/NonExisting/Record"))
                .andExpect(status().isNotFound());
    }
}
