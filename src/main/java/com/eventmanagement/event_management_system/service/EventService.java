package com.eventmanagement.event_management_system.service;

import com.eventmanagement.event_management_system.dto.EventDTO;
import com.eventmanagement.event_management_system.dto.EventWithOrganizersDTO;
import com.eventmanagement.event_management_system.entity.*;
import com.eventmanagement.event_management_system.enums.BookingStatus;
import com.eventmanagement.event_management_system.enums.EventStatus;
import com.eventmanagement.event_management_system.enums.PaymentStatus;
import com.eventmanagement.event_management_system.exception.DuplicateResourceException;
import com.eventmanagement.event_management_system.exception.IllegalStatusException;
import com.eventmanagement.event_management_system.exception.ResourceNotFoundException;
import com.eventmanagement.event_management_system.externalAPIDTO.GeoDTO;
import com.eventmanagement.event_management_system.externalAPIDTO.GeoResult;
import com.eventmanagement.event_management_system.externalAPIDTO.weatherDTO;
import com.eventmanagement.event_management_system.interfaceService.IEventService;
import com.eventmanagement.event_management_system.mapper.EventMapper;
import com.eventmanagement.event_management_system.mapper.EventWithOrganizersMapper;
import com.eventmanagement.event_management_system.repository.CategoryRepository;
import com.eventmanagement.event_management_system.repository.EventRepository;
import com.eventmanagement.event_management_system.repository.OrganizerRepository;
import com.eventmanagement.event_management_system.searchCriteria.EventSearchCriteria;
import com.eventmanagement.event_management_system.specification.EventSpecification;
import com.eventmanagement.event_management_system.validator.EventValidator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService implements IEventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventMapper eventMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private OrganizerRepository organizerRepository;
    @Autowired
    private EventValidator eventValidator;
    @Autowired
    private EventWithOrganizersMapper eventWithOrganizersMapper;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    @Transactional
    public EventDTO create(EventDTO eventDTO) {
        Category category = categoryRepository.findById(eventDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Category not found with id :" + eventDTO.getCategoryId()));
        eventValidator.validateEventDates(eventDTO);
         if (eventRepository.existsByName(eventDTO.getName())) {
            throw new DuplicateResourceException("Event name already exists!");
        }
        Event event = eventMapper.toEntity(eventDTO);
        event.setCategory(category);
        event.setEventStatus(eventDTO.getEventStatus());
         try {
            weatherDTO weather = weatherInfo(eventDTO);
            if (weather != null && weather.getCurrent_weather() != null) {
                event.setTemperature(weather.getCurrent_weather().getTemperature());
                event.setWindspeed(weather.getCurrent_weather().getWindspeed());
            }
        } catch (Exception e) {
            System.err.println("Weather API failed, setting default values. Error: " + e.getMessage());
            event.setTemperature(0.0);
            event.setWindspeed(0.0);
        }

        List<Organizer> organizers = organizerRepository.findAllById(eventDTO.getOrganizerIds());
        if (organizers.size() != eventDTO.getOrganizerIds().size()) {
            throw new ResourceNotFoundException("One or more organizers not found");
        }
        event.getOrganizers().addAll(organizers);
        Event saved = eventRepository.save(event);
        EventDTO result = eventMapper.toDTO(saved);

        return result;
    }

    @Override
    public EventDTO findById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found eith id " + id));
        return eventMapper.toDTO(event);
    }


    @Override
    public List<EventDTO> findAll() {
        List<Event> events = eventRepository.findAll();
        return eventMapper.toDTO(events);
    }



    @Override
    @Transactional
    public EventDTO update(Long id, EventDTO eventDTO) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found eith id " + id));
        Category category = categoryRepository.findById(eventDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Category not found with id :" + eventDTO.getCategoryId()));
        List<Organizer> organizers = organizerRepository.findAllById(eventDTO.getOrganizerIds());
        if (organizers.size() != eventDTO.getOrganizerIds().size())
            throw new ResourceNotFoundException("One or more organizers not found");
        event.setName(eventDTO.getName());
        event.setDescription(eventDTO.getDescription());
        event.setStartDate(eventDTO.getStartDate());
        event.setEndDate(eventDTO.getEndDate());
        event.setLocation(eventDTO.getLocation());
        event.setPrice(eventDTO.getPrice());
        event.setCapacity(eventDTO.getCapacity());
        event.setEventStatus(eventDTO.getEventStatus());
        for (Booking b : event.getBooking()) {
            if (b.getPayment() != null) {
                b.getPayment().setPaymentStatus(PaymentStatus.REFUNDED);
            }
        }

         if (event.getEventStatus() == EventStatus.CANCELLED) {
            throw new IllegalStatusException("Cannot perform action: Event is already CANCELLED");
        }
        event.setCategory(category);
        event.setOrganizers(organizers);
        Event updated = eventRepository.save(event);
        return eventMapper.toDTO(updated);

    }



    @Override
    @Transactional
    public void delete(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        if (event.getEventStatus() == EventStatus.DRAFT || event.getEventStatus() == EventStatus.PUBLISHED) {
            if (event.getBooking() != null) {
                for (Booking b : event.getBooking()) {
                    if (b.getBookingStatus() == BookingStatus.CONFIRMED ||
                            b.getBookingStatus() == BookingStatus.PENDING) {
                        if (b.getPayment() != null) {
                            b.getPayment().setPaymentStatus(PaymentStatus.REFUNDED);
                        }
                        b.setBookingStatus(BookingStatus.CANCELLED);
                    }
                }
            }
        }
        if (event.getReviews() != null) {
            for (Review reviews : event.getReviews()) {
                event.setReviews(null);
            }
        }
        if (event.getOrganizers()!=null){
            throw new IllegalStatusException("event cannot be deleted due to the presence of organizers.");
        }
        eventRepository.delete(event);
    }

    @Override
    public Page<EventDTO> findByPage(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size,sort);
        Page<Event> attendeePage = eventRepository.findAll(pageable);
        return attendeePage.map(eventMapper::toDTO);
    }


    @Override
    public Page<EventDTO> search(EventSearchCriteria eventSearchCriteria, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<Event> specification = EventSpecification.search(eventSearchCriteria);
        Page<Event> page1 = eventRepository.findAll(specification, pageable);
        return page1.map(eventMapper::toDTO);

    }

    @Override
    public List<EventWithOrganizersDTO> findEventWithOrganizers() {
        List<Event> events = eventRepository.findAll();
        return eventWithOrganizersMapper.toDTO(events);
    }

    // weather API
    public weatherDTO weatherInfo(EventDTO eventDTO) {
        String url = "https://geocoding-api.open-meteo.com/v1/search?name=" + eventDTO.getLocation() + "&count=1";
        GeoDTO result = restTemplate.getForObject(url, GeoDTO.class);

        GeoResult city = result.getResults().get(0);
        Double lat = city.getLatitude();
        Double lon = city.getLongitude();

        String url2 = "https://api.open-meteo.com/v1/forecast?latitude=" + lat
                + "&longitude=" + lon + "&current_weather=true";
        weatherDTO weatherDTOResult = restTemplate.getForObject(url2, weatherDTO.class);
        return weatherDTOResult;
    }


}





