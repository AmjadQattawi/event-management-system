package com.eventmanagement.event_management_system.mapper;

import com.eventmanagement.event_management_system.dto.ReviewDTO;
import com.eventmanagement.event_management_system.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper extends BaseMapper<Review, ReviewDTO>{

    @Override
    @Mapping(source = "attendee.id",target = "attendeeId")
    @Mapping(source = "event.id",target = "eventId")
    public ReviewDTO toDTO(Review review);

    @Override
    @Mapping(target = "attendee",ignore = true)
    @Mapping(target = "event",ignore = true)
    public Review toEntity(ReviewDTO reviewDTO);


}
