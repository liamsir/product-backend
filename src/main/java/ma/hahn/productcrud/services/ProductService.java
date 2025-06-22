package ma.hahn.productcrud.services;

import lombok.AllArgsConstructor;
import ma.hahn.productcrud.dtos.ProductDTO;
import ma.hahn.productcrud.entities.Product;
import ma.hahn.productcrud.exceptions.ProductNotFoundException;
import ma.hahn.productcrud.mappers.ProductMapper;
import ma.hahn.productcrud.repositories.ProductRepo;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepo productRepo;
    private final ProductMapper productMapper;



    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepo.findAll();
        return products.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }


    public ProductDTO getProductById(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return productMapper.toDTO(product);
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        Product saved = productRepo.save(product);
        return productMapper.toDTO(saved);
    }

    public ProductDTO updateProduct(Long id, ProductDTO updatedDTO) {
        Product existingProduct = productRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        productMapper.updateEntityFromDTO(updatedDTO, existingProduct);

        Product saved = productRepo.save(existingProduct);
        return productMapper.toDTO(saved);
    }

    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }
}
