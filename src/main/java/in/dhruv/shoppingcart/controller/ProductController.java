package in.dhruv.shoppingcart.controller;

import in.dhruv.shoppingcart.dto.product.ProductRequestDTO;
import in.dhruv.shoppingcart.dto.product.ProductResponseDTO;
import in.dhruv.shoppingcart.entity.Product;
import in.dhruv.shoppingcart.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(
            ProductService productService
    ) {
        this.productService = productService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    private ResponseEntity<ProductResponseDTO> createProduct(
            @RequestBody ProductRequestDTO productRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.createProduct(productRequestDTO));
    }

    @PreAuthorize("hasRole('User', 'ADMIN')")
    @GetMapping("/{id}")
    private ResponseEntity<ProductResponseDTO> getProductById(
            @PathVariable Long id
    ) {
        return ResponseEntity
                .ok(productService.getProductById(id));
    }

    @PreAuthorize("hasRole('User', 'ADMIN')")
    @GetMapping("/all")
    private ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        return ResponseEntity
                .ok(productService.getAllProducts());
    }

    @PreAuthorize("hasRole('User', 'ADMIN')")
    @GetMapping("/category/{categoryId}")
    private ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(
            @PathVariable Long categoryId) {
        return ResponseEntity
                .ok(productService.getProductsByCategory(categoryId));
    }

    @PreAuthorize("hasRole('User', 'ADMIN')")
    @GetMapping("/search")
    private ResponseEntity<List<ProductResponseDTO>> searchProducts(
            @RequestParam String name) {
        return ResponseEntity
                .ok(productService.searchProducts(name));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/update/{id}")
    private ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product product
    ) {
        return ResponseEntity
                .ok(productService.updateProduct(id, product));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    private ResponseEntity<Void> deleteProduct(
            @PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
