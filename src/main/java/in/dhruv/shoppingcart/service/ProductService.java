package in.dhruv.shoppingcart.service;

import in.dhruv.shoppingcart.dto.product.ProductRequest;
import in.dhruv.shoppingcart.dto.product.ProductResponse;
import in.dhruv.shoppingcart.entity.Product;

import java.util.List;

public interface ProductService {

    Product createProduct(Product product);
    Product getProductById(Long id);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(Long categoryId);
    List<Product> searchProducts(String name);
    Product updateProduct(Long id, Product product);
    void deleteProduct(Long id);
    
//    ProductResponse createProduct(ProductRequest request);
//    ProductResponse updateProduct(Long id,
//                                  ProductRequest request);
//    void deleteProduct(Long id);
//    ProductResponse getProduct(Long id);
//    List<ProductResponse> getAllProducts();
//    List<ProductResponse> searchProducts(String keyword);
}
