package com.eventmanagement.event_management_system.service;

import com.eventmanagement.event_management_system.dto.AttendeeDTO;
import com.eventmanagement.event_management_system.dto.BookingDTO;
import com.eventmanagement.event_management_system.entity.Attendee;
import com.eventmanagement.event_management_system.entity.Booking;
import com.eventmanagement.event_management_system.entity.Event;
import com.eventmanagement.event_management_system.entity.Organizer;
import com.eventmanagement.event_management_system.enums.BookingStatus;
import com.eventmanagement.event_management_system.enums.PaymentStatus;
import com.eventmanagement.event_management_system.exception.ResourceNotFoundException;
import com.eventmanagement.event_management_system.searchCriteria.BookingSearchCriteria;
import com.eventmanagement.event_management_system.specification.BookingSpecification;
import com.eventmanagement.event_management_system.validator.BookingValidator;
import com.eventmanagement.event_management_system.interfaceService.IBookingService;
import com.eventmanagement.event_management_system.mapper.BookingMapper;
import com.eventmanagement.event_management_system.repository.AttendeeRepository;
import com.eventmanagement.event_management_system.repository.BookingRepository;
import com.eventmanagement.event_management_system.repository.EventRepository;
import jakarta.transaction.Transactional;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookingService implements IBookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BookingMapper bookingMapper;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private BookingValidator bookingValidator;
    @Autowired
    private AttendeeRepository attendeeRepository;

    @Override
    @Transactional
    public BookingDTO create(BookingDTO bookingDTO){
        //check ids
        Event event=eventRepository.findById(bookingDTO.getEventId())
                .orElseThrow(()->new ResourceNotFoundException("Event not found with id: "+bookingDTO.getEventId()));
        Attendee attendee=attendeeRepository.findById(bookingDTO.getAttendeeId())
                .orElseThrow(()->new ResourceNotFoundException("Attendee not found with id: "+bookingDTO.getAttendeeId()));
      //use bookingValidator ti check
        bookingValidator.checkStatus(event);
        bookingValidator.checkExistsByAttendeeIdAndEventId(bookingDTO);
        bookingValidator.validateCapacity(bookingDTO,event);
        Booking booking=bookingMapper.toEntity(bookingDTO);
        booking.setTotalPrice
                (bookingValidator.getTotalPrice(bookingDTO.getNumberOfTickets(),event.getPrice()));
        event.setCapacity(event.getCapacity()-booking.getNumberOfTickets());
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setEvent(event);
        booking.setAttendee(attendee);
        Booking saved=bookingRepository.save(booking);
        return bookingMapper.toDTO(saved);
    }

    @Override
    public BookingDTO findById(Long id) {
        Booking booking=bookingRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Booking not found with id : " + id));
        return bookingMapper.toDTO(booking);
    }

    @Override
    public List<BookingDTO> findAll() {
        List<Booking> booking=bookingRepository.findAll();
        return bookingMapper.toDTO(booking);
    }

    @Override
    @Transactional
    public BookingDTO update(Long id, BookingDTO bookingDTO) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
        Event event = eventRepository.findById(bookingDTO.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        Attendee attendee = attendeeRepository.findById(bookingDTO.getAttendeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Attendee not found"));

        int oldTickets = booking.getNumberOfTickets();
        event.setCapacity(event.getCapacity() + oldTickets);
        bookingValidator.checkStatus(event);
        bookingValidator.validateCapacity(bookingDTO, event);
        booking.setNumberOfTickets(bookingDTO.getNumberOfTickets());

        if (!bookingDTO.getBookingStatus().equals(BookingStatus.CANCELLED)) {
            event.setCapacity(event.getCapacity() - bookingDTO.getNumberOfTickets());
        }
        if (booking.getBookingStatus().equals(BookingStatus.CANCELLED)) {
            booking.setBookingStatus(bookingDTO.getBookingStatus());
            if (booking.getPayment() != null) {
                booking.getPayment().setPaymentStatus(PaymentStatus.REFUNDED);
            }
        }

        booking.setTotalPrice(bookingValidator.getTotalPrice(bookingDTO.getNumberOfTickets(), event.getPrice()));
        booking.setEvent(event);
        booking.setAttendee(attendee);
        return bookingMapper.toDTO(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id " + id));
        Event event = booking.getEvent();
        if (!booking.getBookingStatus().equals(BookingStatus.CANCELLED)) {
            event.setCapacity(event.getCapacity() + booking.getNumberOfTickets());
        }
        if (booking.getBookingStatus().equals(BookingStatus.CONFIRMED) ||
                booking.getBookingStatus().equals(BookingStatus.PENDING)) {
            if (booking.getPayment() != null) {
                booking.getPayment().setPaymentStatus(PaymentStatus.REFUNDED);
            }
        }
        if (booking.getPayment() != null) {
            booking.getPayment().setBooking(null);
        }

        bookingRepository.delete(booking);
    }


    @Override
    public Page<BookingDTO> findByPage(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size,sort);
        Page<Booking> attendeePage=bookingRepository.findAll(pageable);
        return attendeePage.map(bookingMapper::toDTO);
    }


    @Override
    public Page<BookingDTO> search(BookingSearchCriteria bookingSearchCriteria, int page, int size) {
        Pageable pageable=PageRequest.of(page,size);
        Specification<Booking> specification= BookingSpecification.search(bookingSearchCriteria);
        Page<Booking> page1=bookingRepository.findAll(specification,pageable);
        return page1.map(bookingMapper::toDTO);
    }

}

