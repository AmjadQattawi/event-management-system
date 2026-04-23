package com.eventmanagement.event_management_system.interfaceService;


import com.eventmanagement.event_management_system.dto.OrganizerDTO;
import com.eventmanagement.event_management_system.searchCriteria.OrganizerSearchCriteria;
import com.eventmanagement.event_management_system.service.BaseService;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IOrganizerService extends BaseService<OrganizerDTO,Long> {

    public OrganizerDTO create(OrganizerDTO organizerDTO) ;

    public OrganizerDTO update(Long id, OrganizerDTO organizerDTO) ;

    public Page<OrganizerDTO> search(OrganizerSearchCriteria organizerSearchCriteria,int page,int size);

    public OrganizerDTO updateByEmail(String email,OrganizerDTO organizerDTO);


}
