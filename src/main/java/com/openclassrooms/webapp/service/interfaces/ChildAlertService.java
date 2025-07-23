package com.openclassrooms.webapp.service.interfaces;

import com.openclassrooms.webapp.dto.ChildrenAtAddressDTO;

import java.io.IOException;

public interface ChildAlertService {
    public ChildrenAtAddressDTO getChildrenAtAddress(String address) throws IOException;
}
