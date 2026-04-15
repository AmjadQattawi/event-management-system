package com.eventmanagement.event_management_system.service;

import com.eventmanagement.event_management_system.dto.CategoryDTO;
import com.eventmanagement.event_management_system.dto.CategoryDTODetailed;
import com.eventmanagement.event_management_system.entity.Category;
import com.eventmanagement.event_management_system.exception.IllegalStatusException;
import com.eventmanagement.event_management_system.exception.ResourceNotFoundException;
import com.eventmanagement.event_management_system.interfaceService.ICategoryService;
import com.eventmanagement.event_management_system.mapper.CategoryDetailedMapper;
import com.eventmanagement.event_management_system.mapper.CategoryMapper;
import com.eventmanagement.event_management_system.repository.CategoryRepository;
import com.eventmanagement.event_management_system.searchCriteria.CategorySearchCriteria;
import com.eventmanagement.event_management_system.specification.CategorySpecification;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryDetailedMapper categoryDetailedMapper;

    @Transactional
    @Override
    public CategoryDTO create(CategoryDTO categoryDTO){
        Category category=categoryMapper.toEntity(categoryDTO);
        Category saved =categoryRepository.save(category);
        return categoryMapper.toDTO(saved);
    }

    @Override
    public CategoryDTO findById(Long id){
        Category category=categoryRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Category not found with id : " + id));
        return categoryMapper.toDTO(category);
    }


    @Override
    public List<CategoryDTO> findAll() {
        List<Category> category=categoryRepository.findAll();
        return categoryMapper.toDTO(category);
    }


    @Transactional
    @Override
    public CategoryDTO update(Long id,CategoryDTO categoryDTO){
        Category category=categoryRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Category not found with id : "+ id));
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        Category updated=categoryRepository.save(category);
        return categoryMapper.toDTO(updated);
    }

    @Override
    @Transactional
    public void delete(Long id){
        Category category=categoryRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Category not found with id: "+ id));
        if (categoryRepository.existsByEventsNotEmpty (id))
          throw new IllegalStatusException("This section cannot be deleted! There are currently events associated with it.");
        categoryRepository.delete(category);
    }

    @Override
    public Page<CategoryDTO> findByPage(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size,sort);
        Page<Category> attendeePage=categoryRepository.findAll(pageable);
        return attendeePage.map(categoryMapper::toDTO);
    }



    @Override
    public Page<CategoryDTO> search(CategorySearchCriteria categorySearchCriteria, int page, int size) {
        Pageable pageable=PageRequest.of(page,size);
        Specification<Category> specification= CategorySpecification.search(categorySearchCriteria);
        Page<Category> page1=categoryRepository.findAll(specification,pageable);
        return page1.map(categoryMapper::toDTO);
    }

    @Override
    public List<CategoryDTODetailed> getCategoryDTODetailed(){
        List<Category> categories=categoryRepository.findAll();
        return categoryDetailedMapper.toDTO(categories);
    }


}
