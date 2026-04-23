package com.eventmanagement.event_management_system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AttendeeDTO extends UserDTO{
    @Min(0)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer rewardPoints;

}
