package in.dhruv.shoppingcart.service;

import in.dhruv.shoppingcart.dto.product.ProductRequestDTO;
import in.dhruv.shoppingcart.dto.product.ProductResponseDTO;
import in.dhruv.shoppingcart.entity.Product;

import java.util.List;

public interface ProductService {

    ProductResponseDTO createProduct(
            ProductRequestDTO productRequestDTO
    );
    ProductResponseDTO getProductById(
            Long id
    );
    List<ProductResponseDTO> getAllProducts();
    List<ProductResponseDTO> getProductsByCategory(Long categoryId);
    List<ProductResponseDTO> searchProducts(String name);
    Product updateProduct(
            Long id,
            Product product
    );
    void deleteProduct(Long id);

}
