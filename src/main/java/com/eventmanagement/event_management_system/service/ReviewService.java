package com.eventmanagement.event_management_system.service;

import com.eventmanagement.event_management_system.dto.ReviewDTO;
import com.eventmanagement.event_management_system.entity.Attendee;
import com.eventmanagement.event_management_system.entity.Event;
import com.eventmanagement.event_management_system.entity.Payment;
import com.eventmanagement.event_management_system.entity.Review;
import com.eventmanagement.event_management_system.enums.BookingStatus;
import com.eventmanagement.event_management_system.enums.EventStatus;
import com.eventmanagement.event_management_system.exception.DuplicateResourceException;
import com.eventmanagement.event_management_system.exception.IllegalStatusException;
import com.eventmanagement.event_management_system.exception.ResourceNotFoundException;
import com.eventmanagement.event_management_system.interfaceService.IReviewService;
import com.eventmanagement.event_management_system.mapper.ReviewMapper;
import com.eventmanagement.event_management_system.repository.AttendeeRepository;
import com.eventmanagement.event_management_system.repository.EventRepository;
import com.eventmanagement.event_management_system.repository.ReviewRepository;
import com.eventmanagement.event_management_system.searchCriteria.ReviewSearchCriteria;
import com.eventmanagement.event_management_system.specification.PaymentSpecification;
import com.eventmanagement.event_management_system.specification.ReviewSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final EventRepository eventRepository;
    private final AttendeeRepository attendeeRepository;

    @Override
    @Transactional
    public ReviewDTO create(ReviewDTO reviewDTO) {
        Event event=eventRepository.findById(reviewDTO.getEventId())
         .orElseThrow(()->new ResourceNotFoundException("Event not found with id: "+ reviewDTO.getEventId()));
        Attendee attendee=attendeeRepository.findById(reviewDTO.getAttendeeId())
         .orElseThrow(()->new ResourceNotFoundException("Attendee not found with id: " + reviewDTO.getAttendeeId()));
        Review review=reviewMapper.toEntity(reviewDTO);
        if (event.getEventStatus()!= EventStatus.COMPLETED)
            throw new IllegalStatusException("Event not completed yet");
        if (!reviewRepository.hasConfirmedBooking(reviewDTO.getAttendeeId()
                , reviewDTO.getEventId(), BookingStatus.CONFIRMED)){
            throw new IllegalStatusException("Booking Status must be CONFIRMED " +
                    "(You must have attended the event to evaluate it.)");  }
        if (reviewRepository.existsByAttendeeIdAndEventId(reviewDTO.getAttendeeId(),reviewDTO.getEventId()))
        throw new DuplicateResourceException("You have already reviewed this event");

        review.setEvent(event);
        review.setAttendee(attendee);

        Review saved = reviewRepository.save(review);
        return reviewMapper.toDTO(saved);
    }


    @Override
    public ReviewDTO findById(Long id) {
        Review review=reviewRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Review not found with id:" + id));
        return reviewMapper.toDTO(review);
    }

    @Override
    public List<ReviewDTO> findAll() {
        List<Review> reviews=reviewRepository.findAll();
        return reviewMapper.toDTO(reviews);
    }



    @Override
    @Transactional
    public ReviewDTO update(Long id, ReviewDTO reviewDTO) {
        Review review=reviewRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Review not found with id:" + id));
        Event event=eventRepository.findById(reviewDTO.getEventId())
                .orElseThrow(()->new ResourceNotFoundException("Event not found with id: "+ reviewDTO.getEventId()));
        Attendee attendee=attendeeRepository.findById(reviewDTO.getAttendeeId())
                .orElseThrow(()->new ResourceNotFoundException("Attendee not found with id: " + reviewDTO.getAttendeeId()));
        if (event.getEventStatus()!= EventStatus.COMPLETED)
            throw new IllegalStatusException("Event not completed yet");
        if (!reviewRepository.hasConfirmedBooking(reviewDTO.getAttendeeId()
                , reviewDTO.getEventId(), BookingStatus.CONFIRMED)){
            throw new IllegalStatusException("Booking Status must be CONFIRMED " +
                    "(You must have attended the event to evaluate it.)");
        }
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());

        Review saved=reviewRepository.save(review);
        return reviewMapper.toDTO(saved);
    }



    @Override
    public void delete(Long id) {
        if(!reviewRepository.existsById(id)){
            throw new ResourceNotFoundException("Review not found ");
        }
        reviewRepository.deleteById(id);
    }

    @Override
    public Page<ReviewDTO> findByPage(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size,sort);
        Page<Review> attendeePage=reviewRepository.findAll(pageable);
        return attendeePage.map(reviewMapper::toDTO);
    }

    @Override
    public Page<ReviewDTO> search(ReviewSearchCriteria reviewSearchCriteria, int page, int size) {
        Pageable pageable=PageRequest.of(page,size);
        Specification<Review> specification= ReviewSpecification.search(reviewSearchCriteria);
        Page<Review> page1=reviewRepository.findAll(specification,pageable);
        return page1.map(reviewMapper::toDTO);
    }

}
