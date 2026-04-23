package com.eventmanagement.event_management_system.mapper;

import com.eventmanagement.event_management_system.dto.AdminDTO;
import com.eventmanagement.event_management_system.dto.AttendeeDTO;
import com.eventmanagement.event_management_system.entity.Admin;
import com.eventmanagement.event_management_system.entity.Attendee;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AdminMapper extends BaseMapper<Admin, AdminDTO> {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    void updateAdminFromDto(AdminDTO adminDTO, @MappingTarget Admin admin);

}
