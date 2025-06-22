package ma.hahn.productcrud.controllers;


import ma.hahn.productcrud.dtos.ProductDTO;
import ma.hahn.productcrud.services.ProductService;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import java.util.List;

class ProductControllerTest {

    private ProductService productService;
    private ProductController productController;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        productController = new ProductController(productService);

        productDTO = new ProductDTO(1L, "Test Product", "Desc", 100.0, 10);
    }

    @Test
    void testGetAllProducts() {
        when(productService.getAllProducts()).thenReturn(List.of(productDTO));

        ResponseEntity<List<ProductDTO>> response = productController.getAllProducts();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Test Product", response.getBody().get(0).getName());
    }



    @Test
    void testCreateProduct() {
        when(productService.createProduct(any(ProductDTO.class))).thenReturn(productDTO);

        ResponseEntity<ProductDTO> response = productController.createProduct(productDTO);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Test Product", response.getBody().getName());
    }

    @Test
    void testUpdateProduct() {
        when(productService.updateProduct(eq(1L), any(ProductDTO.class))).thenReturn(productDTO);

        ResponseEntity<ProductDTO> response = productController.updateProduct(1L, productDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Test Product", response.getBody().getName());
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productService).deleteProduct(1L);

        ResponseEntity<Void> response = productController.deleteProduct(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(productService).deleteProduct(1L);
    }
}
