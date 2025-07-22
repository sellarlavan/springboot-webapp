package com.openclassrooms.webapp.dto;

import java.util.List;
import java.util.Map;

public class FloodStationDTO {
    private Map<String, List<ResidentInfoDTO>> households;

    public FloodStationDTO(Map<String, List<ResidentInfoDTO>> households) {
        this.households = households;
    }

    public Map<String, List<ResidentInfoDTO>> getHouseholds() {
        return households;
    }

    public void setHouseholds(Map<String, List<ResidentInfoDTO>> households) {
        this.households = households;
    }
}