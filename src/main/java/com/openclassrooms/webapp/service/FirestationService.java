package com.openclassrooms.webapp.service;

import com.openclassrooms.webapp.dto.FirestationCoverage;
import com.openclassrooms.webapp.model.Firestation;

import java.io.IOException;

public interface FirestationService {
    Firestation createFirestation(Firestation firestation) throws IOException;

    Firestation updateFirestation(Firestation firestation) throws IOException;

    boolean deleteByAddress(String address) throws IOException;

    boolean deleteByStationNumber(int stationNumber) throws IOException;

    FirestationCoverage getPersonsCoveredByStation(int stationNumber) throws IOException ;
}
