package com.eventmanagement.event_management_system.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryDTODetailed extends BaseDTO{

    @NotBlank(message = "name is required")
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    private List<EventDTO> events;

}
