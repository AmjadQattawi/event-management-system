package com.eventmanagement.event_management_system.interfaceService;

import com.eventmanagement.event_management_system.dto.CategoryDTO;
import com.eventmanagement.event_management_system.searchCriteria.CategorySearchCriteria;
import com.eventmanagement.event_management_system.service.BaseService;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICategoryService  extends BaseService<CategoryDTO,Long> {

    public CategoryDTO create(CategoryDTO categoryDTO) ;

    public CategoryDTO update(Long id, CategoryDTO categoryDTO) ;

    public Page<CategoryDTO> search(CategorySearchCriteria categorySearchCriteria,int page,int size);

    }
