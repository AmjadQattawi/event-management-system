package com.eventmanagement.event_management_system.mapper;

import com.eventmanagement.event_management_system.dto.OrganizerDTO;
import com.eventmanagement.event_management_system.entity.Organizer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrganizerMapper extends BaseMapper<Organizer, OrganizerDTO> {

    @Override
    @Mapping(source = "password",target = "password")
    public Organizer toEntity(OrganizerDTO organizerDTO);
}
