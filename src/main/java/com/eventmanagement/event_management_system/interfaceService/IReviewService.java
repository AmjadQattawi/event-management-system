package com.eventmanagement.event_management_system.interfaceService;

import com.eventmanagement.event_management_system.dto.ReviewDTO;
import com.eventmanagement.event_management_system.searchCriteria.ReviewSearchCriteria;
import com.eventmanagement.event_management_system.service.BaseService;
import org.springframework.data.domain.Page;

public interface IReviewService extends BaseService<ReviewDTO,Long> {

    public ReviewDTO create(ReviewDTO reviewDTO);

    public ReviewDTO update(Long id, ReviewDTO reviewDTO) ;

    public Page<ReviewDTO> search(ReviewSearchCriteria reviewSearchCriteria,int page,int size);

}
