package com.eventmanagement.event_management_system.mapper;

import com.eventmanagement.event_management_system.dto.AttendeeDTO;
import com.eventmanagement.event_management_system.entity.Attendee;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AttendeeMapper extends BaseMapper<Attendee, AttendeeDTO>{

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateAttendeeFromDto(AttendeeDTO attendeeDTO, @MappingTarget Attendee attendee);

}
