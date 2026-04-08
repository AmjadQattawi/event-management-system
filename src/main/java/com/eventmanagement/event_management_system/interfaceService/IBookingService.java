package com.eventmanagement.event_management_system.interfaceService;

import com.eventmanagement.event_management_system.dto.BookingDTO;
import com.eventmanagement.event_management_system.searchCriteria.BookingSearchCriteria;
import com.eventmanagement.event_management_system.service.BaseService;
import org.springframework.data.domain.Page;

public interface IBookingService extends BaseService<BookingDTO,Long> {

    public BookingDTO create(BookingDTO bookingDTO);

    public BookingDTO update(Long id, BookingDTO bookingDTO) ;

    public Page<BookingDTO> search(BookingSearchCriteria bookingSearchCriteria,int page,int size);

}
