package com.openclassrooms.webapp.service.interfaces;

import com.openclassrooms.webapp.dto.CommunityEmailDTO;
import java.io.IOException;

public interface CommunityService {
    public CommunityEmailDTO getEmailsByCity(String city) throws IOException;
}
