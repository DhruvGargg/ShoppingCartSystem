package in.dhruv.shoppingcart.service.impl;

import in.dhruv.shoppingcart.dto.product.ProductRequestDTO;
import in.dhruv.shoppingcart.dto.product.ProductResponseDTO;
import in.dhruv.shoppingcart.entity.Category;
import in.dhruv.shoppingcart.entity.Product;
import in.dhruv.shoppingcart.exception.ResourceNotFoundException;
import in.dhruv.shoppingcart.mapper.ProductMapper;
import in.dhruv.shoppingcart.repository.CategoryRepository;
import in.dhruv.shoppingcart.repository.ProductRepository;
import in.dhruv.shoppingcart.service.CategoryService;
import in.dhruv.shoppingcart.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ProductServiceImplementation implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private static final Logger logger =
            Logger.getLogger(ProductServiceImplementation.class.getName());

    public ProductServiceImplementation(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            ProductMapper productMapper
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "productById", allEntries = true),
            @CacheEvict(cacheNames = "allProducts", allEntries = true),
            @CacheEvict(cacheNames = "productsByCategory", allEntries = true),
            @CacheEvict(cacheNames = "productSearch", allEntries = true),
            @CacheEvict(cacheNames = "paginatedProducts", allEntries = true)
    })
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        Product product = new Product();
        product.setName(productRequestDTO.getName());
        product.setDescription(productRequestDTO.getDescription());
        product.setPrice(productRequestDTO.getPrice());
        product.setStock(productRequestDTO.getStock());
        Category category = categoryRepository
                .findById(productRequestDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        product.setCategory(category);
        product.setCreatedAt(LocalDateTime.now());
        productRepository.save(product);
        return productMapper.toDTO(product);
    }

    @Override
//    @Cacheable(
//            cacheNames = "productById",
//            key = "#id"
//    )
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return productMapper
                .toDTO(product);
    }

    @Override
    @Cacheable(
            cacheNames = "allProducts",
            key = "'all'"
    )
    public List<ProductResponseDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products
                .stream()
                .map(productMapper::toDTO)
                .toList();
    }

    @Override
    @Cacheable(
            cacheNames = "productsByCategory",
            key = "#categoryId"
    )
    public List<ProductResponseDTO> getProductsByCategory(Long categoryId) {
        List<Product> products = productRepository
                .findByCategoryId(categoryId);
        return products
                .stream()
                .map(productMapper::toDTO)
                .toList();
    }

    @Override
    @Cacheable(
            cacheNames = "productSearch",
            key = "#name.toLowerCase().trim()"
    )
    public List<ProductResponseDTO> searchProducts(String name) {
        List<Product> products = productRepository
                .findByNameContainingIgnoreCase(name);
        return products
                .stream()
                .map(productMapper::toDTO)
                .toList();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "productById", allEntries = true),
            @CacheEvict(cacheNames = "allProducts", allEntries = true),
            @CacheEvict(cacheNames = "productsByCategory", allEntries = true),
            @CacheEvict(cacheNames = "productSearch", allEntries = true),
            @CacheEvict(cacheNames = "paginatedProducts", allEntries = true)
    })
    public Product updateProduct(Long id, Product product) {
        Product updatedProduct = productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        updatedProduct.setName(product.getName());
        updatedProduct.setDescription(product.getDescription());
        updatedProduct.setPrice(product.getPrice());
        updatedProduct.setStock(product.getStock());
        return productRepository.save(updatedProduct);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "productById", allEntries = true),
            @CacheEvict(cacheNames = "allProducts", allEntries = true),
            @CacheEvict(cacheNames = "productsByCategory", allEntries = true),
            @CacheEvict(cacheNames = "productSearch", allEntries = true),
            @CacheEvict(cacheNames = "paginatedProducts", allEntries = true)
    })
    public void deleteProduct(Long id) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        productRepository.delete(product);
    }

    @Override
    @Cacheable(
            cacheNames = "paginatedProducts",
            key = "'page=' + #page + ':size=' + #size + ':sort=' + #sortBy + ':direction=' + #direction"
    )
    public Page<ProductResponseDTO> getAllProducts(
        int page,
        int size,
        String sortBy,
        String direction
    ) {
        Sort sort = direction
                .equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository
                .findAll(pageable)
                .map(productMapper::toDTO);
    }
}
