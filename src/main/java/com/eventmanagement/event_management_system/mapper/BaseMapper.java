package com.eventmanagement.event_management_system.mapper;

import com.eventmanagement.event_management_system.dto.AttendeeDTO;
import com.eventmanagement.event_management_system.entity.Attendee;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

public interface BaseMapper<E,D> {

    public D toDTO(E entity);

    public E toEntity(D dto);

    public List<D> toDTO(List<E> eList);


}
