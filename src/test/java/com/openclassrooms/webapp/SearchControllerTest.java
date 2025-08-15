package com.openclassrooms.webapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testChildAlertWithAddressWithChildrenShouldReturnChildrenList() throws Exception {
        mockMvc.perform(get("/childAlert")
                        .param("address", "1509 Culver St")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.children").isArray())
                .andExpect(jsonPath("$.children", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.children[0].firstName").value("Tenley"))
                .andExpect(jsonPath("$.otherHouseholdMembers[0].lastName").value("Boyd"));
    }

    // TEST CHILD ALERT
    @Test
    public void testChildAlertWithAddressWithoutChildrenShouldReturnNoContent() throws Exception {
        mockMvc.perform(get("/childAlert")
                        .param("address", "112 Steppes Pl")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testChildAlertWithUnknownAddressShouldReturnNoContent() throws Exception {
        mockMvc.perform(get("/childAlert")
                        .param("address", "123 Fake Street")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    //TEST PHONE ALERT
    @Test
    public void testPhoneAlertWithValidStationShouldReturnPhoneNumbers() throws Exception {
        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0]").value("841-874-6512"))
                .andExpect(jsonPath("$[1]").value("841-874-8547"));
    }

    @Test
    public void testPhoneAlertWithUnknownStationShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", "999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPhoneAlertWithValidStationButNoResidentsShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", "5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // TEST FIRE
    @Test
    public void testFireWithValidAddressShouldReturnResidentsAndStationNumber() throws Exception {
        mockMvc.perform(get("/fire")
                        .param("address", "892 Downing Ct")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stationNumber").value(2))
                .andExpect(jsonPath("$.persons").isArray())
                .andExpect(jsonPath("$.persons.length()").value(greaterThan(0)))
                .andExpect(jsonPath("$.persons[0].firstName").value("Sophia"))
                .andExpect(jsonPath("$.persons[0].phone").value("841-874-7878"))
                .andExpect(jsonPath("$.persons[0].age").value("37"));
    }

    @Test
    public void testFireWithUnknownAddressShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/fire")
                        .param("address", "123 Street")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFireWithValidAddressButNoResidentsShouldReturnEmptyBody() throws Exception {
        mockMvc.perform(get("/fire")
                        .param("address", "123 Empty Street")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.persons").isEmpty());
    }

    @Test
    public void testFireEndpoint_withMissingAddress_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/fire")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // TEST FLOOD
    @Test
    public void testFloodStationWithValidStationsReturnsGroupedHouseholds() throws Exception {
        mockMvc.perform(get("/flood/stations")
                        .param("stations", "1", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.households").isMap())

                .andExpect(jsonPath("$.households['951 LoneTree Rd']").isArray())
                .andExpect(jsonPath("$.households['951 LoneTree Rd'][0].firstName").value("Eric"))
                .andExpect(jsonPath("$.households['951 LoneTree Rd'][0].phone").value("841-874-7458"))
                .andExpect(jsonPath("$.households['951 LoneTree Rd'][0].age").value("80"));
    }

    @Test
    public void testFloodStationWithUnknownStationReturnsEmpty() throws Exception {
        mockMvc.perform(get("/flood/stations")
                        .param("stations", "999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.households").isEmpty());
    }

    @Test
    void testFloodStationWithoutStationsParamReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/flood/stations"))
                .andExpect(status().isBadRequest());
    }

    // TEST PERSON INFO
    @Test
    public void testPersonInfoWithValidLastNameReturnsPersonList() throws Exception {
        mockMvc.perform(get("/personInfolastName")
                        .param("lastName", "Boyd")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].lastName").value("Boyd"));
    }

    @Test
    public void testPersonInfoWithUnknownLastNameReturnsNotFound() throws Exception {
        mockMvc.perform(get("/personInfolastName")
                        .param("lastName", "Inconnu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void testPersonInfoWithoutLastNameParamReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/personInfolastName"))
                .andExpect(status().isBadRequest());
    }

    // TEST COMMUNITY
    @Test
    public void testGetCommunityEmailwithValidCityshouldReturnEmails() throws Exception {
        mockMvc.perform(get("/communityEmail")
                        .param("city", "Culver")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.emails").isArray())
                .andExpect(jsonPath("$.emails", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.emails", hasItem("jaboyd@email.com")));
    }

    @Test
    public void testGetCommunityEmailwithUnknownCityshouldReturnEmptyBody() throws Exception {
        mockMvc.perform(get("/communityEmail")
                        .param("city", "UnknownCity"))
                .andExpect(status().isOk())
                .andExpect(content().string(emptyString()));
    }

    @Test
    void testCommunityEmailWithoutCityParamReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/communityEmail"))
                .andExpect(status().isBadRequest());
    }
}
