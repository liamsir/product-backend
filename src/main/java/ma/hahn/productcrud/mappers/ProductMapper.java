package ma.hahn.productcrud.mappers;

import lombok.NoArgsConstructor;
import ma.hahn.productcrud.dtos.ProductDTO;
import ma.hahn.productcrud.entities.Product;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class ProductMapper {

    public ProductDTO toDTO(Product product) {
        if (product == null) return null;

        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        return dto;
    }

    public Product toEntity(ProductDTO dto) {
        if (dto == null) return null;

        Product product = new Product();
        product.setId(dto.getId()); // attention : en création id=null, en update on peut l’ignorer aussi
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        return product;
    }


    public void updateEntityFromDTO(ProductDTO dto, Product product) {
        if (dto == null || product == null)
            return;

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
    }
}
