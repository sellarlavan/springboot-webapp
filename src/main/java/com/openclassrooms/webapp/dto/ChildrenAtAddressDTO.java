package com.openclassrooms.webapp.dto;

import java.util.List;

public class ChildrenAtAddressDTO {
    private List<ChildAlert> children;
    private List<PersonDTO> householdMembers; // ou crée un DTO allégé si tu veux

    public ChildrenAtAddressDTO(List<ChildAlert> children, List<PersonDTO> householdMembers) {
        this.children = children;
        this.householdMembers = householdMembers;
    }

    public List<ChildAlert> getChildren() {
        return children;
    }

    public void setChildren(List<ChildAlert> children) {
        this.children = children;
    }

    public List<PersonDTO> getOtherHouseholdMembers() {
        return householdMembers;
    }

    public void setOtherHouseholdMembers(List<PersonDTO> otherHouseholdMembers) {
        this.householdMembers = otherHouseholdMembers;
    }
}
