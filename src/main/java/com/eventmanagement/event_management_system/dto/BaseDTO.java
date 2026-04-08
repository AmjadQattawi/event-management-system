package com.eventmanagement.event_management_system.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public abstract class BaseDTO {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
