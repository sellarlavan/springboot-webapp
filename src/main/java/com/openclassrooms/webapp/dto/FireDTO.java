package com.openclassrooms.webapp.dto;

import java.util.List;

public class FireDTO {
    private List<PersonInfoDTO> persons;
    private Integer stationNumber;

    public FireDTO() {
    }

    public FireDTO(List<PersonInfoDTO> persons, Integer stationNumber) {
        this.persons = persons;
        this.stationNumber = stationNumber;
    }

    public List<PersonInfoDTO> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonInfoDTO> persons) {
        this.persons = persons;
    }

    public Integer getStationNumber() {
        return stationNumber;
    }

    public void setStationNumber(Integer stationNumber) {
        this.stationNumber = stationNumber;
    }
}
