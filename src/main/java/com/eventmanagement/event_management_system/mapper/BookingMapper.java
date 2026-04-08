package com.eventmanagement.event_management_system.mapper;

import com.eventmanagement.event_management_system.dto.BookingDTO;
import com.eventmanagement.event_management_system.entity.Booking;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BookingMapper extends BaseMapper<Booking, BookingDTO>{

    @Override
    @Mapping(source = "attendee.id",target = "attendeeId")
    @Mapping(source = "event.id",target = "eventId")
    public BookingDTO toDTO(Booking booking);

    @Override
    @Mapping(target = "attendee",ignore = true)
    @Mapping(target = "event",ignore = true)
    @Mapping(target = "payment", ignore = true)
    public Booking toEntity(BookingDTO bookingDTO);




}
