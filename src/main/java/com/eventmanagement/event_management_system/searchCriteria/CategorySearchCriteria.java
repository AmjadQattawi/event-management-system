package com.eventmanagement.event_management_system.searchCriteria;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class CategorySearchCriteria {

    private String name;
    private String description;

}
