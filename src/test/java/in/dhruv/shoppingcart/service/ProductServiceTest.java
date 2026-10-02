package in.dhruv.shoppingcart.service;

import in.dhruv.shoppingcart.dto.product.ProductRequestDTO;
import in.dhruv.shoppingcart.dto.product.ProductResponseDTO;
import in.dhruv.shoppingcart.entity.Category;
import in.dhruv.shoppingcart.entity.Product;
import in.dhruv.shoppingcart.exception.ResourceNotFoundException;
import in.dhruv.shoppingcart.mapper.ProductMapper;
import in.dhruv.shoppingcart.repository.CategoryRepository;
import in.dhruv.shoppingcart.repository.ProductRepository;
import in.dhruv.shoppingcart.service.impl.ProductServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImplementation productServiceImplementation;

    @Test
    void shouldCreateProductSuccessfully()
    {
        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        productRequestDTO.setCategoryId(1L);
        productRequestDTO.setName("Electronics");
        productRequestDTO.setDescription("Electronics Description");
        productRequestDTO.setPrice(new BigDecimal("100.00"));
        productRequestDTO.setStock(10);

        Product product = new Product();
        product.setName("Electronics");
        product.setDescription("Electronics Description");
        product.setPrice(new BigDecimal("100.00"));
        product.setStock(10);
        product.setCategory(category);

        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setId(1L);
        productResponseDTO.setName("Electronics");
        productResponseDTO.setDescription("Electronics Description");
        productResponseDTO.setPrice(new BigDecimal("100.00"));
        productResponseDTO.setStock(10);

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        when(productMapper.toDTO(any(Product.class)))
                .thenReturn(productResponseDTO);

        ProductResponseDTO result =
                productServiceImplementation.createProduct(productRequestDTO);

        assertNotNull(result);
        assertEquals("Electronics", result.getName());

        verify(categoryRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
        verify(productMapper).toDTO(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFound()
    {
        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        productRequestDTO.setCategoryId(1L);

        when(categoryRepository.findById(1L))
        .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productServiceImplementation.createProduct(productRequestDTO)
        );

        verify(categoryRepository).findById(1L);

        verify(productRepository, never())
        .save(any(Product.class));
    }

    @Test
    void shouldReturnProductById()
    {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product Name");
        product.setDescription("Product Description");
        product.setPrice(new BigDecimal("100.00"));
        product.setStock(10);

        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setId(1L);
        productResponseDTO.setName("Product Name");
        productResponseDTO.setDescription("Product Description");
        productResponseDTO.setPrice(new BigDecimal("100.00"));
        productResponseDTO.setStock(10);

        when(productRepository.findById(1L))
        .thenReturn(Optional.of(product));

        when(productMapper.toDTO(any(Product.class)))
        .thenReturn(productResponseDTO);

        ProductResponseDTO result = productServiceImplementation.getProductById(1L);

        assertNotNull(result);
        assertEquals("Product Name", result.getName());

        verify(productRepository).findById(1L);
        verify(productMapper).toDTO(product);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound()
    {
        when(productRepository.findById(1L))
        .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productServiceImplementation.getProductById(1L)
        );

        verify(productRepository).findById(1L);

        verify(productMapper, never())
        .toDTO(any(Product.class));
    }

    @Test
    void shouldReturnAllProducts()
    {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setDescription("Product 1 Description");
        product1.setPrice(new BigDecimal("100.00"));
        product1.setStock(10);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setDescription("Product 2 Description");
        product2.setPrice(new BigDecimal("200.00"));
        product2.setStock(20);

        ProductResponseDTO productResponseDTO1 = new ProductResponseDTO();
        productResponseDTO1.setId(1L);
        productResponseDTO1.setName("Product 1");
        productResponseDTO1.setDescription("Product 1 Description");
        productResponseDTO1.setPrice(new BigDecimal("100.00"));
        productResponseDTO1.setStock(10);

        ProductResponseDTO productResponseDTO2 = new ProductResponseDTO();
        productResponseDTO2.setId(2L);
        productResponseDTO2.setName("Product 2");
        productResponseDTO2.setDescription("Product 2 Description");
        productResponseDTO2.setPrice(new BigDecimal("200.00"));
        productResponseDTO2.setStock(20);

        List<Product> products = List.of(product1, product2);

        when(productRepository.findAll())
        .thenReturn(products);

        when(productMapper.toDTO(product1))
        .thenReturn(productResponseDTO1);

        when(productMapper.toDTO(product2))
        .thenReturn(productResponseDTO2);

        List<ProductResponseDTO> result = productServiceImplementation.getAllProducts();

        assertEquals(2, result.size());

        verify(productRepository).findAll();

        verify(productMapper, times(2))
        .toDTO(any(Product.class));
    }

    @Test
    void shouldReturnProductsByCategory()
    {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product Name");
        product.setDescription("Product Description");
        product.setPrice(new BigDecimal("100.00"));
        product.setStock(10);

        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setCategoryName("Product Name");
        productResponseDTO.setDescription("Product Description");
        productResponseDTO.setPrice(new BigDecimal("100.00"));
        productResponseDTO.setStock(10);

        when(productRepository.findByCategoryId(1L))
        .thenReturn(List.of(product));

        when(productMapper.toDTO(product))
        .thenReturn(productResponseDTO);

        List<ProductResponseDTO> result =
                productServiceImplementation
                        .getProductsByCategory(1L);

        assertEquals(1, result.size());

        verify(productRepository).findByCategoryId(1L);
    }

    @Test
    void shouldSearchProductsSuccessfully()
    {
        String searchTerm = "Prod";

        Product product = new Product();
        product.setId(1L);
        product.setName("Product Name");
        product.setDescription("Product Description");
        product.setPrice(new BigDecimal("100.00"));
        product.setStock(10);

        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setId(1L);
        productResponseDTO.setName("Product Name");
        productResponseDTO.setDescription("Product Description");
        productResponseDTO.setPrice(new BigDecimal("100.00"));
        productResponseDTO.setStock(10);

        when(productRepository.findByNameContainingIgnoreCase(searchTerm))
        .thenReturn(List.of(product));

        when(productMapper.toDTO(product))
        .thenReturn(productResponseDTO);

        List<ProductResponseDTO> result =
                productServiceImplementation.searchProducts(searchTerm);

        assertEquals(1, result.size());

        verify(productRepository)
                .findByNameContainingIgnoreCase(searchTerm);
    }

    @Test
    void shouldUpdateProductSuccessfully()
    {
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setName("Existing Product");
        existingProduct.setDescription("Existing Product Description");
        existingProduct.setPrice(new BigDecimal("100.00"));
        existingProduct.setStock(10);

        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setName("Updated Product");
        updatedProduct.setDescription("Updated Product Description");
        updatedProduct.setPrice(new BigDecimal("200.00"));
        updatedProduct.setStock(20);

        when(productRepository.findById(1L))
        .thenReturn(Optional.of(existingProduct));

        when(productRepository.save(any(Product.class)))
        .thenReturn(existingProduct);

        Product result =
                productServiceImplementation.updateProduct(1L, updatedProduct);

        assertNotNull(result);

        assertEquals("Updated Product", result.getName());
        assertEquals("Updated Product Description", result.getDescription());
        assertEquals(new BigDecimal("200.00"), result.getPrice());
        assertEquals(20, result.getStock());

        verify(productRepository).findById(1L);
        verify(productRepository).save(existingProduct);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingProduct()
    {
        Product Product = new Product();

        when(productRepository.findById(1L))
        .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productServiceImplementation.updateProduct(1L, Product)
        );

        verify(productRepository).findById(1L);

        verify(productRepository, never())
        .save(any(Product.class));
    }

    @Test
    void shouldDeleteProductSuccessfully()
    {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product Name");
        product.setDescription("Product Description");
        product.setPrice(new BigDecimal("100.00"));
        product.setStock(10);

        when(productRepository.findById(1L))
        .thenReturn(Optional.of(product));

        doNothing().when(productRepository).delete(product);

        productServiceImplementation.deleteProduct(1L);

        verify(productRepository).findById(1L);
        verify(productRepository).delete(product);
    }

    @Test
    void shouldThrowExceptionWhenDeletingProduct()
    {
        when(productRepository.findById(1L))
        .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productServiceImplementation.deleteProduct(1L)
        );

        verify(productRepository).findById(1L);

        verify(productRepository, never())
        .delete(any(Product.class));
    }

    @Test
    void shouldReturnPaginatedProducts()
    {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product Name");
        product.setDescription("Product 1 Description");
        product.setPrice(new BigDecimal("100.00"));
        product.setStock(10);

        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setId(1L);
        productResponseDTO.setName("Product Name");
        productResponseDTO.setDescription("Product 1 Description");
        productResponseDTO.setPrice(new BigDecimal("100.00"));
        productResponseDTO.setStock(10);

        Pageable pageable = PageRequest.of(
                0,
                5,
                Sort.by("name").ascending()
        );

        Page<Product> page =
                new PageImpl<>(List.of(product));

        when(productRepository.findAll(pageable))
                .thenReturn(page);

        when(productMapper.toDTO(product))
                .thenReturn(productResponseDTO);

        Page<ProductResponseDTO> result =
                productServiceImplementation.getAllProducts(
                        0,
                        5,
                        "name",
                         "asc"
                );

        assertEquals(1, result.getContent().size());

        verify(productRepository).findAll(pageable);
    }
}
