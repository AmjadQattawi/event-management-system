package com.eventmanagement.event_management_system.interfaceService;

import com.eventmanagement.event_management_system.dto.ReviewDTO;
import com.eventmanagement.event_management_system.dto.UserDTO;
import com.eventmanagement.event_management_system.searchCriteria.ReviewSearchCriteria;
import com.eventmanagement.event_management_system.service.BaseService;
import org.springframework.data.domain.Page;

public interface IUserService extends BaseService<UserDTO,Long> {


    public UserDTO create(UserDTO userDTO);

    public UserDTO update(Long id, UserDTO userDTO) ;

    //public Page<UserDTO> search(ReviewSearchCriteria reviewSearchCriteria, int page, int size);



}
