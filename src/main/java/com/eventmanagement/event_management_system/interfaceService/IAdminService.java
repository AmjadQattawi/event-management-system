package com.eventmanagement.event_management_system.interfaceService;

import com.eventmanagement.event_management_system.dto.AdminDTO;
import com.eventmanagement.event_management_system.dto.BookingDTO;
import com.eventmanagement.event_management_system.dto.EventDTO;
import com.eventmanagement.event_management_system.entity.Admin;
import com.eventmanagement.event_management_system.searchCriteria.AdminSearchCriteria;
import com.eventmanagement.event_management_system.searchCriteria.EventSearchCriteria;
import com.eventmanagement.event_management_system.service.BaseService;
import org.springframework.data.domain.Page;

public interface IAdminService extends BaseService<AdminDTO, Long> {

    public AdminDTO create(AdminDTO adminDTO);

    public AdminDTO update(Long id, AdminDTO adminDTO);

    public Page<AdminDTO> search(AdminSearchCriteria adminSearchCriteria, int page, int size);


}
