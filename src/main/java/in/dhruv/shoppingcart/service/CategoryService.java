package in.dhruv.shoppingcart.service;

import in.dhruv.shoppingcart.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CategoryService {

    Category createCategory(Category category);
    Category getCategoryById(Long id);
    List<Category> getAllCategories();
    Category updateCategory(Long id, Category category);
    void deleteCategory(Long id);
}
