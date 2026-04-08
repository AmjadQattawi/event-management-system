package com.eventmanagement.event_management_system.interfaceService;

import com.eventmanagement.event_management_system.dto.AttendeeDTO;
import com.eventmanagement.event_management_system.searchCriteria.AttendeeSearshCriteria;
import com.eventmanagement.event_management_system.service.BaseService;
import org.springframework.data.domain.Page;


public interface IAttendeeService extends BaseService<AttendeeDTO,Long> {

    public AttendeeDTO create(AttendeeDTO attendeeDTO) ;

    public AttendeeDTO update(Long id, AttendeeDTO attendeeDTO) ;

    public Page<AttendeeDTO> search(AttendeeSearshCriteria attendeeSearshCriteria,int page,int size);

}
