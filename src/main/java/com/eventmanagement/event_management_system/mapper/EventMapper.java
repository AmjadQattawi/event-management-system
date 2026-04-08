package com.eventmanagement.event_management_system.mapper;

import com.eventmanagement.event_management_system.dto.AdminDTO;
import com.eventmanagement.event_management_system.dto.EventDTO;
import com.eventmanagement.event_management_system.entity.Admin;
import com.eventmanagement.event_management_system.entity.Event;
import com.eventmanagement.event_management_system.entity.Organizer;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface EventMapper extends BaseMapper<Event, EventDTO> {

    @Named("mapOrganizersToIds")
    default List<Long> mapOrganizersToIds(List<Organizer> organizers) {
        return organizers == null ? null : organizers.stream().map(Organizer::getId).toList();
    }
    @Override
    @Mapping(source = "category.id",target = "categoryId")
    @Mapping(source = "organizers",target = "organizerIds",qualifiedByName = "mapOrganizersToIds")
    @Mapping(source = "temperature", target = "temperature")
    @Mapping(source = "windspeed", target = "windspeed")
    public EventDTO toDTO(Event event);

     default Long map(Organizer organizer) {
        return organizer != null ? organizer.getId() : null;
    }

    @Override
    @Mapping(target = "category",ignore = true)
    @Mapping(target = "organizers",ignore = true)
    public Event toEntity(EventDTO eventDTO);




}
