package in.dhruv.shoppingcart.controller;

import in.dhruv.shoppingcart.entity.Cart;
import in.dhruv.shoppingcart.entity.Category;
import in.dhruv.shoppingcart.service.CategoryService;
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
    private ResponseEntity<Category> createCategory(
            @RequestBody Category category
    ) {
        return ResponseEntity.ok(
                categoryService.createCategory(category)
        );
    }

    @GetMapping("/{id}")
    private ResponseEntity<Category> getCategoryById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                categoryService.getCategoryById(id)
        );
    }

    @GetMapping("/all")
    private ResponseEntity<Iterable<Category>> getAllCategories() {
        return ResponseEntity.ok(
                categoryService.getAllCategories()
        );
    }

    @PutMapping("/update/{id}")
    private ResponseEntity<Category> updateCategory(
            @PathVariable Long id,
            @RequestBody  Category category
    ) {
        return ResponseEntity.ok(
                categoryService.updateCategory(id, category)
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
