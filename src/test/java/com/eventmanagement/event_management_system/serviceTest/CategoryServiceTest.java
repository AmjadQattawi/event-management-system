package com.eventmanagement.event_management_system.serviceTest;


import com.eventmanagement.event_management_system.dto.CategoryDTO;
import com.eventmanagement.event_management_system.entity.Category;
import com.eventmanagement.event_management_system.exception.ResourceNotFoundException;
import com.eventmanagement.event_management_system.mapper.CategoryMapper;
import com.eventmanagement.event_management_system.repository.CategoryRepository;
import com.eventmanagement.event_management_system.service.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CategoryService Unit Tests")
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;


    @Test
    @DisplayName("create: should save and return CategoryDTO")
    void create_ShouldSaveAndReturnCategoryDTO() {
        // Given
        CategoryDTO dto = new CategoryDTO();
        dto.setName("Music");
        Category entity = new Category();
        Category saved = new Category();

        when(categoryMapper.toEntity(dto)).thenReturn(entity);
        when(categoryRepository.save(entity)).thenReturn(saved);
        when(categoryMapper.toDTO(saved)).thenReturn(dto);

        CategoryDTO result = categoryService.create(dto);

        // Then
        assertThat(result).isNotNull();
        verify(categoryRepository).save(entity);
    }


    @Test
    @DisplayName("findById: should return CategoryDTO when category exists")
    void findById_ShouldReturnCategoryDTO_WhenCategoryExists() {
        // Given
        Category category = new Category();
        CategoryDTO dto = new CategoryDTO();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDTO(category)).thenReturn(dto);

        CategoryDTO result = categoryService.findById(1L);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("findById: should throw ResourceNotFoundException when category not found")
    void findById_ShouldThrowResourceNotFoundException_WhenCategoryNotFound() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }
}