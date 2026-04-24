package com.eventmanagement.event_management_system.serviceTest;




import com.eventmanagement.event_management_system.dto.ReviewDTO;
import com.eventmanagement.event_management_system.entity.Attendee;
import com.eventmanagement.event_management_system.entity.Event;
import com.eventmanagement.event_management_system.entity.Review;
import com.eventmanagement.event_management_system.enums.EventStatus;
import com.eventmanagement.event_management_system.exception.IllegalStatusException;
import com.eventmanagement.event_management_system.exception.ResourceNotFoundException;
import com.eventmanagement.event_management_system.mapper.ReviewMapper;
import com.eventmanagement.event_management_system.repository.AttendeeRepository;
import com.eventmanagement.event_management_system.repository.EventRepository;
import com.eventmanagement.event_management_system.repository.ReviewRepository;
import com.eventmanagement.event_management_system.service.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReviewService Unit Tests")
public class ReviewServiceTest {

    @Mock private ReviewRepository reviewRepository;
    @Mock private ReviewMapper reviewMapper;
    @Mock private EventRepository eventRepository;
    @Mock private AttendeeRepository attendeeRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    @DisplayName("update: should throw IllegalStatusException when event is not completed")
    void update_ShouldThrowIllegalStatusException_WhenEventNotCompleted() {
        ReviewDTO dto = buildReviewDTO();
        Event event = new Event();
        event.setEventStatus(EventStatus.PUBLISHED);

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(new Review()));
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(attendeeRepository.findById(1L)).thenReturn(Optional.of(new Attendee()));

        assertThatThrownBy(() -> reviewService.update(1L, dto))
                .isInstanceOf(IllegalStatusException.class)
                .hasMessageContaining("Event not completed yet");
    }

    @Test
    @DisplayName("delete: should delete review when review exists")
    void delete_ShouldDeleteReview_WhenReviewExists() {
        when(reviewRepository.existsById(1L)).thenReturn(true);

        reviewService.delete(1L);

        verify(reviewRepository).deleteById(1L);
    }

    @Test
    @DisplayName("delete: should throw ResourceNotFoundException when review not found")
    void delete_ShouldThrowResourceNotFoundException_WhenReviewNotFound() {
        when(reviewRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> reviewService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(reviewRepository, never()).deleteById(any());
    }


    private ReviewDTO buildReviewDTO() {
        ReviewDTO dto = new ReviewDTO();
        dto.setEventId(1L);
        dto.setAttendeeId(1L);
        dto.setRating(4);
        dto.setComment("Great event!");
        return dto;
    }

}
