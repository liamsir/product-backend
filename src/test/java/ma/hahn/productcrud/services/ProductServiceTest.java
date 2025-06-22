package ma.hahn.productcrud.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ma.hahn.productcrud.dtos.ProductDTO;
import ma.hahn.productcrud.entities.Product;
import ma.hahn.productcrud.exceptions.ProductNotFoundException;
import ma.hahn.productcrud.mappers.ProductMapper;
import ma.hahn.productcrud.repositories.ProductRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

class ProductServiceTest {

    @Mock
    private ProductRepo productRepo;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);


        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(100.0);
        product.setQuantity(10);

        productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setName("Test Product");
        productDTO.setDescription("Test Description");
        productDTO.setPrice(100.0);
        productDTO.setQuantity(10);
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = List.of(product);
        when(productRepo.findAll()).thenReturn(products);

        List<ProductDTO> result = productService.getAllProducts();

        assertEquals(1, result.size());
        verify(productRepo, times(1)).findAll();
    }

    @Test
    void testGetProductById_Found() {
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDTO(product)).thenReturn(productDTO);

        ProductDTO dto = productService.getProductById(1L);

        assertNotNull(dto);
        assertEquals("Test Product", dto.getName());
        verify(productRepo).findById(1L);
        verify(productMapper).toDTO(product);
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
        verify(productRepo).findById(1L);
    }

    @Test
    void testCreateProduct() {
        when(productMapper.toEntity(productDTO)).thenReturn(product);
        when(productRepo.save(product)).thenReturn(product);
        when(productMapper.toDTO(product)).thenReturn(productDTO);

        ProductDTO created = productService.createProduct(productDTO);

        assertNotNull(created);
        assertEquals("Test Product", created.getName());
        verify(productMapper).toEntity(productDTO);
        verify(productRepo).save(product);
        verify(productMapper).toDTO(product);
    }

    @Test
    void testUpdateProduct_shouldUpdateAndReturnDTO() {
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));

        // Simulate update with new values
        ProductDTO updatedDTO = new ProductDTO();
        updatedDTO.setName("Updated Name");
        updatedDTO.setDescription("Updated Desc");
        updatedDTO.setPrice(199.99);
        updatedDTO.setQuantity(99);

        doAnswer(inv -> {
            ProductDTO dto = inv.getArgument(0);
            Product prod = inv.getArgument(1);
            prod.setName(dto.getName());
            prod.setDescription(dto.getDescription());
            prod.setPrice(dto.getPrice());
            prod.setQuantity(dto.getQuantity());
            return null;
        }).when(productMapper).updateEntityFromDTO(any(), any());

        when(productRepo.save(product)).thenReturn(product);
        when(productMapper.toDTO(product)).thenReturn(updatedDTO);

        ProductDTO result = productService.updateProduct(1L, updatedDTO);

        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Desc", result.getDescription());
        assertEquals(199.99, result.getPrice());
        assertEquals(99, result.getQuantity());

        verify(productRepo).findById(1L);
        verify(productRepo).save(product);
        verify(productMapper).toDTO(product);
    }









    @Test
    void testUpdateProduct_NotFound() {
        when(productRepo.findById(1L)).thenReturn(Optional.empty());

        ProductDTO updatedDTO = new ProductDTO();
        assertThrows(ProductNotFoundException.class,
                () -> productService.updateProduct(1L, updatedDTO));
        verify(productRepo).findById(1L);
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productRepo).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepo).deleteById(1L);
    }
}