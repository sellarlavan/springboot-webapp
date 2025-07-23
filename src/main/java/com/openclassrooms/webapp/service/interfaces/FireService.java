package com.openclassrooms.webapp.service.interfaces;

import com.openclassrooms.webapp.dto.FireDTO;

import java.io.IOException;

public interface FireService {
    public FireDTO getResidentsAndStation(String address) throws IOException;
}
