package com.openclassrooms.webapp.service.interfaces;

import com.openclassrooms.webapp.dto.FloodStationDTO;
import java.io.IOException;
import java.util.List;

public interface FloodService {
    public FloodStationDTO getHouseholdsByStations(List<Integer> stationNumbers) throws IOException;
}
