package in.dhruv.shoppingcart.service;

import in.dhruv.shoppingcart.dto.category.CategoryRequestDTO;
import in.dhruv.shoppingcart.dto.category.CategoryResponseDTO;
import in.dhruv.shoppingcart.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CategoryService {

    CategoryResponseDTO createCategory(
            CategoryRequestDTO categoryRequestDTO
    );
    CategoryResponseDTO getCategoryById(
            Long id
    );
    List<CategoryResponseDTO> getAllCategories();
    CategoryResponseDTO updateCategory(
            Long id,
            CategoryRequestDTO categoryRequestDTO
    );
    void deleteCategory(
            Long id
    );
}
