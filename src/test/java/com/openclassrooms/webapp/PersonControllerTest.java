package com.openclassrooms.webapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.webapp.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreatePersonShouldReturnCreated() throws Exception {
        Person newPerson = new Person();
        newPerson.setFirstName("Test");
        newPerson.setLastName("User");
        newPerson.setAddress("123 Test Person Street");
        newPerson.setCity("TestCity");
        newPerson.setZip("12345");
        newPerson.setPhone("123-456-7890");
        newPerson.setEmail("testuser@example.com");

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPerson)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("User"))
                .andExpect(jsonPath("$.email").value("testuser@example.com"));
    }

    @Test
    public void testUpdateExistingPersonShouldReturnUpdatedPerson() throws Exception {
        Person updated = new Person();
        updated.setFirstName("firstUpdate");
        updated.setLastName("lastUpdate");
        updated.setAddress("123 Updated Street");
        updated.setCity("UpdatedCity");
        updated.setZip("12345");
        updated.setPhone("000-000-0000");
        updated.setEmail("updated@email.com");

        mockMvc.perform(put("/person/firstUpdate/lastUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("123 Updated Street"))
                .andExpect(jsonPath("$.city").value("UpdatedCity"))
                .andExpect(jsonPath("$.zip").value("12345"))
                .andExpect(jsonPath("$.phone").value("000-000-0000"))
                .andExpect(jsonPath("$.email").value("updated@email.com"));
    }

    @Test
    public void testUpdateNonExistingPersonShouldReturnNotFound() throws Exception {
        Person updated = new Person();
        updated.setFirstName("invalidFirst");
        updated.setLastName("invalidLast");
        updated.setAddress("invalidAddress");
        updated.setCity("invalid");
        updated.setZip("00000");
        updated.setPhone("000-000-0000");
        updated.setEmail("invalid@email.com");

        mockMvc.perform(put("/person/invalidFirst/invalidLast")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteExistingPersonShouldReturnNoContent() throws Exception {
        Person personToDelete = new Person();
        personToDelete.setFirstName("ToDelete");
        personToDelete.setLastName("Person");
        personToDelete.setAddress("123 Delete Person Street");
        personToDelete.setCity("Delete City");
        personToDelete.setZip("00000");
        personToDelete.setPhone("000-000-0000");
        personToDelete.setEmail("delete@gmail.com");

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personToDelete)))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/person/ToDelete/Person"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteNonExistingPersonShouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/person/NotExist/Person"))
                .andExpect(status().isNotFound());
    }
}
