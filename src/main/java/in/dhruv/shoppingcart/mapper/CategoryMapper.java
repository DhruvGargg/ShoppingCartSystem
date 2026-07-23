package in.dhruv.shoppingcart.mapper;

import in.dhruv.shoppingcart.dto.category.CategoryRequestDTO;
import in.dhruv.shoppingcart.dto.category.CategoryResponseDTO;
import in.dhruv.shoppingcart.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequestDTO categoryRequestDTO)
    {
        Category category = new Category();
        category.setName(categoryRequestDTO.getName());
        category.setDescription(categoryRequestDTO.getDescription());
        return category;
    }

    public CategoryResponseDTO toResponseDTO(Category category)
    {
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setId(category.getId());
        categoryResponseDTO.setName(category.getName());
        categoryResponseDTO.setDescription(category.getDescription());
        return categoryResponseDTO;
    }
}
