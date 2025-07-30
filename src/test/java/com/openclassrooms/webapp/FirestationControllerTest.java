package com.openclassrooms.webapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.webapp.model.Firestation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FirestationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateFirestationShouldReturnCreatedFirestation() throws Exception {
        Firestation firestation = new Firestation();
        firestation.setAddress("123 Firestation Create Street");
        firestation.setStation(5);

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firestation)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.address", is("123 Firestation Create Street")))
                .andExpect(jsonPath("$.station", is(5)));
    }

    @Test
    public void testUpdateFirestationShouldReturnUpdatedFirestation() throws Exception {
        Firestation updatedFirestation = new Firestation();
        updatedFirestation.setAddress("123 Firestation Update Street");
        updatedFirestation.setStation(10);

        mockMvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFirestation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address", is("123 Firestation Update Street")))
                .andExpect(jsonPath("$.station", is(10)));
    }

    @Test
    public void testUpdateFirestationWithInvalidAddressShouldReturnNotFound() throws Exception {
        Firestation nonExisting = new Firestation();
        nonExisting.setAddress("123 Invalid Address");
        nonExisting.setStation(42);

        mockMvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nonExisting)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteFirestationByAddressShouldReturnNoContent() throws Exception {
        Firestation tempFirestation = new Firestation();
        tempFirestation.setAddress("123 Firestation Delete Street");
        tempFirestation.setStation(12);

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tempFirestation)))
                .andExpect(status().isCreated());


        mockMvc.perform(delete("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tempFirestation)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteFirestationByStationNumberShouldReturnNoContent() throws Exception {
        Firestation tempFirestation = new Firestation();
        tempFirestation.setAddress("345 Firestation Delete Street");
        tempFirestation.setStation(13);

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tempFirestation)))
                .andExpect(status().isCreated());

        Firestation deleteRequest = new Firestation();
        deleteRequest.setAddress("");
        deleteRequest.setStation(13);

        mockMvc.perform(delete("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteRequest)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteFirestationWithEmptyRequestShouldReturnBadRequest() throws Exception {
        Firestation invalidRequest = new Firestation();
        invalidRequest.setAddress("");
        invalidRequest.setStation(0);

        mockMvc.perform(delete("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteFirestationWithInvalidAddressShouldReturnNotFound() throws Exception {
        Firestation request = new Firestation();
        request.setAddress("Fake Address");
        request.setStation(0);

        mockMvc.perform(delete("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetPersonsByStationWithExistingStationAndResidentsShouldReturnPeopleList() throws Exception {
        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.persons").isArray())
                .andExpect(jsonPath("$.persons.length()").value(greaterThan(0)))
                .andExpect(jsonPath("$.numberOfAdults").value("5"))
                .andExpect(jsonPath("$.numberOfChildren").value("1"));
    }

    @Test
    public void testGetPersonsByStationWithUnknownStationShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetPersonsByStationWithValidStationButNoResidentsShouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.persons").isEmpty())
                .andExpect(jsonPath("$.numberOfAdults").value(0))
                .andExpect(jsonPath("$.numberOfChildren").value(0));
    }
}
