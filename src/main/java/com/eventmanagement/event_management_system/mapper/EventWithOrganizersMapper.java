package com.eventmanagement.event_management_system.mapper;

import com.eventmanagement.event_management_system.dto.EventWithOrganizersDTO;
import com.eventmanagement.event_management_system.entity.Event;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = OrganizerMapper.class)
public interface EventWithOrganizersMapper extends BaseMapper<Event, EventWithOrganizersDTO> {



}
