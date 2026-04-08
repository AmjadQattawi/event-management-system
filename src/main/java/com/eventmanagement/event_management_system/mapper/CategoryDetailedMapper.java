package com.eventmanagement.event_management_system.mapper;

import com.eventmanagement.event_management_system.dto.CategoryDTODetailed;
import com.eventmanagement.event_management_system.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = EventMapper.class)
public interface CategoryDetailedMapper extends BaseMapper<Category, CategoryDTODetailed> {


}
