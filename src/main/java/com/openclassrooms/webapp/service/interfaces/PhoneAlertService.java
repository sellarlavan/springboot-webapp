package com.openclassrooms.webapp.service.interfaces;

import java.io.IOException;
import java.util.List;

public interface PhoneAlertService {
    public List<String> getPhoneNumbersByStation(int stationNumber) throws IOException;
}
