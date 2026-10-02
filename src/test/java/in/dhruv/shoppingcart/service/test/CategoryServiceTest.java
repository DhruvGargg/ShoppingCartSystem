package in.dhruv.shoppingcart.service.test;

import in.dhruv.shoppingcart.dto.category.CategoryRequestDTO;
import in.dhruv.shoppingcart.dto.category.CategoryResponseDTO;
import in.dhruv.shoppingcart.entity.Category;
import in.dhruv.shoppingcart.mapper.CategoryMapper;
import in.dhruv.shoppingcart.repository.CategoryRepository;
import in.dhruv.shoppingcart.service.impl.CategoryServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private CategoryServiceImplementation categoryServiceImplementation;

    @Test
    void shouldCreateCategorySuccessfully()
    {
        CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO();
        categoryRequestDTO.setName("Electronics(T)");
        categoryRequestDTO.setDescription("Electronics Items(T)");

        Category category = new Category();
        category.setName("Electronics(T)");
        category.setDescription("Electronics Items(T)");

        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setId(1L);
        categoryResponseDTO.setName("Electronics(T)");
        categoryResponseDTO.setDescription("Electronics Items(T)");

        when(categoryRepository.save(any(Category.class)))
                .thenReturn(category);

        when(categoryMapper.toDTO(any(Category.class)))
                .thenReturn(categoryResponseDTO);

        CategoryResponseDTO result =
                categoryServiceImplementation.createCategory(categoryRequestDTO);

        assertNotNull(result);
        assertEquals("Electronics(T)", result.getName());

        verify(categoryRepository, times(1))
                .save(any(Category.class));

        verify(categoryMapper, times(1))
                .toDTO(any(Category.class));
    }

    @Test
    void shouldReturnCategoryWhenIdExists()
    {
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Electronics(T)");
        category.setDescription("Electronics Items(T)");

        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setId(categoryId);
        categoryResponseDTO.setName("Electronics(T)");
        categoryResponseDTO.setDescription("Electronics Items(T)");

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));

        when(categoryMapper.toDTO(any(Category.class)))
                .thenReturn(categoryResponseDTO);

        CategoryResponseDTO result =
                categoryServiceImplementation.getCategoryById(categoryId);

        assertNotNull(result);
        assertEquals(categoryId, result.getId());
        assertEquals("Electronics(T)", result.getName());

        verify(categoryRepository, times(1))
                .findById(categoryId);

        verify(categoryMapper, times(1))
                .toDTO(category);
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFound()
    {
        Long categoryId = 1L;

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());

        RuntimeException runtimeException = assertThrows(
                RuntimeException.class,
                () -> categoryServiceImplementation.getCategoryById(categoryId)
        );

        assertEquals(
                "Category not found",
                runtimeException.getMessage()
        );

        verify(categoryRepository, times(1))
                .findById(categoryId);

        verify(categoryMapper, never())
                .toDTO(any(Category.class));
    }

    @Test
    void shouldReturnAllCategories()
    {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Electronics(T)");
        category1.setDescription("Electronics Items(T)");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Books(T)");
        category2.setDescription("Books Items(T)");

        CategoryResponseDTO categoryResponseDTO1 = new CategoryResponseDTO();
        categoryResponseDTO1.setId(1L);
        categoryResponseDTO1.setName("Electronics(T)");
        categoryResponseDTO1.setDescription("Electronics Items(T)");

        CategoryResponseDTO categoryResponseDTO2 = new CategoryResponseDTO();
        categoryResponseDTO2.setId(2L);
        categoryResponseDTO2.setName("Books(T)");
        categoryResponseDTO2.setDescription("Books Items(T)");

        List<Category> categories = List.of(category1, category2);

        when(categoryRepository.findAll())
                .thenReturn(categories);

        when(categoryMapper.toDTO(category2))
                .thenReturn(categoryResponseDTO2);

        when(categoryMapper.toDTO(category1))
                .thenReturn(categoryResponseDTO1);

        List<CategoryResponseDTO> result =
                categoryServiceImplementation.getAllCategories();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Electronics(T)", result.get(0).getName());
        assertEquals("Books(T)", result.get(1).getName());

        verify(categoryRepository, times(1))
                .findAll();

        verify(categoryMapper, times(1))
                .toDTO(any(Category.class));
    }

    @Test
    void ShouldReturnEmptyList()
    {
        when(categoryRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<CategoryResponseDTO> result =
                categoryServiceImplementation.getAllCategories();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(categoryRepository, times(1))
                .findAll();

        verify(categoryMapper, never())
                .toDTO(any(Category.class));
    }

    @Test
    void shouldUpdateCategorySuccessfully() {

        Long categoryId = 1L;

        CategoryRequestDTO requestDTO = new CategoryRequestDTO();
        requestDTO.setName("Updated Electronics");
        requestDTO.setDescription("Updated description");

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Electronics");
        category.setDescription("Old description");

        CategoryResponseDTO responseDTO = new CategoryResponseDTO();
        responseDTO.setId(categoryId);
        responseDTO.setName("Updated Electronics");
        responseDTO.setDescription("Updated description");

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));

        when(categoryRepository.save(any(Category.class)))
                .thenReturn(category);

        when(categoryMapper.toDTO(any(Category.class)))
                .thenReturn(responseDTO);

        CategoryResponseDTO result =
                categoryServiceImplementation.updateCategory(categoryId, requestDTO);

        assertNotNull(result);
        assertEquals(categoryId, result.getId());

        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDTO(category);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingCategory() {

        Long categoryId = 1L;

        CategoryRequestDTO requestDTO = new CategoryRequestDTO();
        requestDTO.setName("Electronics");

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> categoryServiceImplementation.updateCategory(
                        categoryId,
                        requestDTO
                )
        );

        assertEquals(
                "Category not found",
                exception.getMessage()
        );

        verify(categoryRepository, never())
                .save(any(Category.class));
    }

    @Test
    void shouldThrowExceptionWhenDeletingCategory() {

        Long categoryId = 1L;

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> categoryServiceImplementation.deleteCategory(categoryId)
        );

        assertEquals("Category not found", exception.getMessage());

        verify(categoryRepository, times(1))
                .findById(categoryId);

        verify(categoryRepository, never())
                .delete(any(Category.class));
    }
}
