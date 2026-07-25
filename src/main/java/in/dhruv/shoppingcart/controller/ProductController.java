package in.dhruv.shoppingcart.controller;

import in.dhruv.shoppingcart.entity.Product;
import in.dhruv.shoppingcart.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    private ResponseEntity<Product> createProduct(Product product) {
        return ResponseEntity
                .status(201)
                .body(productService.createProduct(product));
    }

    @GetMapping("/{id}")
    private ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity
                .ok(productService.getProductById(id));
    }

    @GetMapping("/all")
    private ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity
                .ok(productService.getAllProducts());
    }

    @GetMapping("/category/{categoryId}")
    private ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity
                .ok(productService.getProductsByCategory(categoryId));
    }

    @GetMapping("/search")
    private ResponseEntity<List<Product>> searchProducts(@RequestParam String name) {
        return ResponseEntity
                .ok(productService.searchProducts(name));
    }

    @PutMapping("/{id}")
    private ResponseEntity<Product> updateProduct(@PathVariable Long id, Product product) {
        return ResponseEntity
                .ok(productService.updateProduct(id, product));
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
