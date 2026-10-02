package in.dhruv.shoppingcart.controller;

import in.dhruv.shoppingcart.dto.category.CategoryRequestDTO;
import in.dhruv.shoppingcart.dto.category.CategoryResponseDTO;
import in.dhruv.shoppingcart.entity.Cart;
import in.dhruv.shoppingcart.entity.Category;
import in.dhruv.shoppingcart.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    private ResponseEntity<CategoryResponseDTO> createCategory(
            @Valid @RequestBody CategoryRequestDTO categoryRequestDTO
    ) {
        return ResponseEntity.ok(
                categoryService.createCategory(categoryRequestDTO)
        );
    }

    @GetMapping("/{id}")
    private ResponseEntity<CategoryResponseDTO> getCategoryById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                categoryService.getCategoryById(id)
        );
    }

    @GetMapping("/all")
    private ResponseEntity<Iterable<CategoryResponseDTO>> getAllCategories() {
        return ResponseEntity.ok(
                categoryService.getAllCategories()
        );
    }

    @PutMapping("/update/{id}")
    private ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryRequestDTO categoryrequestDTO
    ) {
        return ResponseEntity.ok(
                categoryService.updateCategory(id, categoryrequestDTO)
        );
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<Void> deleteCategory(
            @PathVariable Long id
    ) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
