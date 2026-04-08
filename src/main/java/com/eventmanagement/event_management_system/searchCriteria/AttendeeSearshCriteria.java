package com.eventmanagement.event_management_system.searchCriteria;

import com.eventmanagement.event_management_system.enums.UserStatus;
import lombok.Data;

@Data
public class AttendeeSearshCriteria {

    private String name;
    private String email;
    private String phone;
    private UserStatus userStatus;
    private Integer rewardPoints;

}
