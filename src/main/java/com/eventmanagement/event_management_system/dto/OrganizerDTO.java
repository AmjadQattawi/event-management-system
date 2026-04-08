package com.eventmanagement.event_management_system.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrganizerDTO extends UserDTO{

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String companyName;

}
