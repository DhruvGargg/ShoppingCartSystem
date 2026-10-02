package in.dhruv.shoppingcart.servicetest;

import in.dhruv.shoppingcart.entity.Category;
import in.dhruv.shoppingcart.repository.CategoryRepository;
import in.dhruv.shoppingcart.service.impl.CategoryServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplementationTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImplementation categoryServiceImplementation;

    @Test
    void shouldCreateCategory() {

        Category category = new Category();
        category.setName("Test Category");

        when(categoryRepository.save(any(Category.class)))
                .thenReturn(category);

//        Category result = categoryServiceImplementation.createCategory(category);
    }
}
