package in.dhruv.shoppingcart.service;

import in.dhruv.shoppingcart.mapper.ProductMapper;
import in.dhruv.shoppingcart.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;
}
