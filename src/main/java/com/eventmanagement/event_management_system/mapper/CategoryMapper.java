package com.eventmanagement.event_management_system.mapper;

import com.eventmanagement.event_management_system.dto.CategoryDTO;
import com.eventmanagement.event_management_system.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends BaseMapper<Category, CategoryDTO> {

}
