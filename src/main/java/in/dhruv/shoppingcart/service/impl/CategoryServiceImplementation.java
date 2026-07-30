package in.dhruv.shoppingcart.service.impl;

import in.dhruv.shoppingcart.dto.category.CategoryRequestDTO;
import in.dhruv.shoppingcart.dto.category.CategoryResponseDTO;
import in.dhruv.shoppingcart.entity.Category;
import in.dhruv.shoppingcart.mapper.CategoryMapper;
import in.dhruv.shoppingcart.repository.CategoryRepository;
import in.dhruv.shoppingcart.service.CategoryService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class CategoryServiceImplementation implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImplementation(
            CategoryRepository categoryRepository,
            CategoryMapper categoryMapper
    ) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryResponseDTO createCategory(
            CategoryRequestDTO categoryRequestDTO
    ) {
        Category category = new Category();
        category.setName(categoryRequestDTO.getName());
        category.setDescription(categoryRequestDTO.getDescription());
        categoryRepository.save(category);
        return categoryMapper.toResponseDTO(category);
    }

    @Override
    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return categoryMapper.toResponseDTO(category);
    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository
                .findAll()
                .stream()
                .map(categoryMapper::toResponseDTO)
                .toList();
    }

    @Override
    public Category updateCategory(
            Long id,
            CategoryRequestDTO categoryRequestDTO
    ) {
        Category categoryToUpdate =
                categoryRepository
                        .findById(id)
                        .orElseThrow(() -> new RuntimeException("Category not found"));
        categoryToUpdate.setName(categoryToUpdate.getName());
        categoryToUpdate.setDescription(categoryToUpdate.getDescription());
        categoryToUpdate.setId(categoryToUpdate.getId());
        categoryToUpdate.setProducts(categoryRequestDTO.getP);
        return categoryRepository.save(categoryToUpdate);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }
}