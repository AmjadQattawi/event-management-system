package com.eventmanagement.event_management_system.searchCriteria;

import lombok.Data;

@Data
public class OrganizerSearchCriteria {

    private String companyName;

    private String taxId;

    private Boolean verified;


}
