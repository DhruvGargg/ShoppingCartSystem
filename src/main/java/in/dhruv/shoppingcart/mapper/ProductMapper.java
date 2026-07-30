package in.dhruv.shoppingcart.mapper;


import in.dhruv.shoppingcart.dto.product.ProductRequestDTO;
import in.dhruv.shoppingcart.dto.product.ProductResponseDTO;
import in.dhruv.shoppingcart.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequestDTO productRequestDTO) {

        return null;
    }

    public ProductResponseDTO toDTO(Product product) {
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setId(product.getId());
        productResponseDTO.setName(product.getName());
        productResponseDTO.setDescription(product.getDescription());
        productResponseDTO.setPrice(product.getPrice());
        productResponseDTO.setStock(product.getStock());
        return productResponseDTO;
    }
}
