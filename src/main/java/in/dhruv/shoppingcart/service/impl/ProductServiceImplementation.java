package in.dhruv.shoppingcart.service.impl;

import in.dhruv.shoppingcart.dto.product.ProductRequestDTO;
import in.dhruv.shoppingcart.dto.product.ProductResponseDTO;
import in.dhruv.shoppingcart.entity.Category;
import in.dhruv.shoppingcart.entity.Product;
import in.dhruv.shoppingcart.mapper.ProductMapper;
import in.dhruv.shoppingcart.repository.CategoryRepository;
import in.dhruv.shoppingcart.repository.ProductRepository;
import in.dhruv.shoppingcart.service.CategoryService;
import in.dhruv.shoppingcart.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImplementation implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    ProductServiceImplementation(ProductRepository productRepository,
                                 CategoryRepository categoryRepository,
                                 ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        Product product = new Product();
        product.setName(productRequestDTO.getName());
        product.setDescription(productRequestDTO.getDescription());
        product.setPrice(productRequestDTO.getPrice());
        product.setStock(productRequestDTO.getStock());

        Category category = categoryRepository
                .findById(productRequestDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);
        return productMapper.toDTO(product);
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {
        return productMapper
                .toDTO(productRepository
                        .findById(id)
                        .orElseThrow(() -> new RuntimeException("Product not found")));
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products
                .stream()
                .map(productMapper::toDTO)
                .toList();
    }

    @Override
    public List<ProductResponseDTO> getProductsByCategory(Long categoryId) {
        List<Product> products = productRepository
                .findByCategoryId(categoryId);
        return products
                .stream()
                .map(productMapper::toDTO)
                .toList();
    }

    @Override
    public List<ProductResponseDTO> searchProducts(String name) {
        List<Product> products = productRepository
                .findByNameContainingIgnoreCase(name);
        return products
                .stream()
                .map(productMapper::toDTO)
                .toList();
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        Product updatedProduct = productRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        updatedProduct.setName(product.getName());
        updatedProduct.setDescription(product.getDescription());
        updatedProduct.setPrice(product.getPrice());
        updatedProduct.setStock(product.getStock());
        return productRepository.save(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }
}
