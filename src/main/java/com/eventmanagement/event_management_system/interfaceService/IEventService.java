package com.eventmanagement.event_management_system.interfaceService;

import com.eventmanagement.event_management_system.dto.EventDTO;
import com.eventmanagement.event_management_system.searchCriteria.EventSearchCriteria;
import com.eventmanagement.event_management_system.service.BaseService;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IEventService extends BaseService<EventDTO, Long> {

    public EventDTO create(EventDTO eventDTO) ;

    public EventDTO update(Long id, EventDTO eventDTO) ;

    public Page<EventDTO> search(EventSearchCriteria eventSearchCriteria, int page, int size);



}
