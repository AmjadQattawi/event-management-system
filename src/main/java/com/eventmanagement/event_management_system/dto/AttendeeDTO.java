package com.eventmanagement.event_management_system.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AttendeeDTO extends UserDTO{
    @Min(0)
    private Integer rewardPoints;

}
