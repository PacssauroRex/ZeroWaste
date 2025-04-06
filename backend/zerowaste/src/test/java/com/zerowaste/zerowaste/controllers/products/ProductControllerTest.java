package com.zerowaste.zerowaste.controllers.products;


import java.time.LocalDate;

import com.zerowaste.controllers.products.ProductController;
import com.zerowaste.dtos.products.CreateProductDTO;
import com.zerowaste.dtos.products.EditProductDTO;
import com.zerowaste.dtos.products.GetProductsResponseBodyDTO;
import com.zerowaste.models.product.Product;
import com.zerowaste.models.product.ProductCategory;
import com.zerowaste.models.product.ProductStatus;
import com.zerowaste.services.products.CreateProductService;
import com.zerowaste.services.products.DeleteProductService;
import com.zerowaste.services.products.EditProductService;
import com.zerowaste.services.products.GetExpiringProductsService;
import com.zerowaste.services.products.GetProductIdService;
import com.zerowaste.services.products.GetProductService;
import com.zerowaste.services.products.SetProductStatusService;
import com.zerowaste.services.products.exceptions.ProductNotAvailableException;
import com.zerowaste.services.products.exceptions.ProductNotFoundException;
import com.zerowaste.utils.Constants;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CreateProductService createProductService;

    @Mock
    private DeleteProductService deleteProductService;

    @Mock
    private EditProductService editProductService;

    @Mock
    private GetProductIdService getProductIdService;

    @Mock 
    private GetProductService getProductService;

    @Mock
    private SetProductStatusService setProductStatusService;

    @Mock
    private GetExpiringProductsService getExpiringProductsService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void TestCreateProduct_Success() throws Exception {
        //Criando DTO
        CreateProductDTO dto = new CreateProductDTO(
            "Teste", 
            "Teste", 
            "Marca teste", 
            ProductCategory.BAKERY.getCategory(), 
            10.0, 
            10, 
            LocalDate.now().plusDays(1)
        );

        //Mockando comportamento do service
        doNothing().when(createProductService).execute(dto);

        //Executando controller
        ResponseEntity<Map<String, String>> response = productController.createProduct(dto);

        //Verificando
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Product created successfully", response.getBody().get(Constants.MESSAGE));
    }

    @Test
    void TestEditProduct_Success() throws Exception {
        //Criando DTO
        EditProductDTO dto = new EditProductDTO(
            "Teste", 
            "Teste", 
            "Marca teste", 
            ProductCategory.BAKERY.getCategory(), 
            10.0, 
            9.0,
            10, 
            LocalDate.now().plusDays(1),
            Set.of(1L)
        );

        //Mockando comportamento do service
        doNothing().when(editProductService).execute(productId, dto);

        //Executando controller
        ResponseEntity<Map<String, String>> response = productController.editProduct(productId, dto);

        //Verificando
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Produto editado com sucesso!", response.getBody().get(Constants.MESSAGE));
    }

    @Test
    void TestDeleteProduct_Success() throws Exception {
        // Quando o serviço DeleteProductService é chamado, ele não faz nada (simulamos sucesso)
        doNothing().when(deleteProductService).execute(anyLong());

        // Então, executamos a requisição DELETE para deletar o produto
        mockMvc.perform(delete("/products/{id}", 1L))
                .andExpect(status().isOk()) // Espera-se um código 200
                .andExpect(jsonPath("$." + Constants.MESSAGE).value("Produto deletado com sucesso!")); // A mensagem retornada deve ser "Produto deletado com sucesso!"
        
        // Verifica que o serviço foi chamado uma vez com o ID do produto
        verify(deleteProductService, times(1)).execute((1L));
    }

    @Test
    void TestGetProductById_Success() throws Exception {
        // Dado um produto existente
        Product product = new Product(
            1L, 
            "Produto Teste", 
            "Descrição do Produto", 
            "Marca Teste", 
            ProductCategory.BAKERY, 
            100.0, 
            null, 
            100, 
            LocalDate.now().plusDays(1),
            ProductStatus.AVALIABLE,
            null, 
            null, 
            null
        );

        // Quando o serviço GetProductIdService é chamado, ele retorna o produto
        when(getProductIdService.execute(anyLong())).thenReturn(product);

        // Então, executamos a requisição GET para buscar o produto
        mockMvc.perform(get("/products/{id}", 1L))
                .andExpect(status().isOk()) // Espera-se um código 200
                .andExpect(jsonPath("$.product.name").value("Produto Teste")) // Verifica o nome do produto
                .andExpect(jsonPath("$.product.description").value("Descrição do Produto")); // Verifica a descrição do produto
        
        // Verifica que o serviço foi chamado uma vez com o ID do produto
        verify(getProductIdService, times(1)).execute((1L));
    }

    @Test
    void TestGetAllProducts_Success() throws Exception {
        // Dado uma lista de produtos
        List<Product> products = List.of(
            new Product(
                1L, 
                "Produto Teste", 
                "Descrição do Produto", 
                "Marca Teste", 
                ProductCategory.BAKERY, 
                100.0, 
                null, 
                100, 
                LocalDate.now().plusDays(1), 
                ProductStatus.AVALIABLE,
                null, 
                null, 
                null
            ),
            new Product(
                2L, 
                "Produto Teste 2", 
                "Descrição do Produto 2", 
                "Marca Teste 2", 
                ProductCategory.HYGIENE, 
                50.0,
                null, 
                200, 
                LocalDate.now().plusDays(2), 
                ProductStatus.AVALIABLE,
                null,
                null, 
                null
            )
        );

         var productsDTO = products.stream()
            .map(product -> new GetProductsResponseBodyDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getBrand(),
                product.getCategory().getCategory(),
                product.getUnitPrice(),
                product.getPromotionPrice(),
                product.getStock(),
                product.getExpiresAt(),
                product.getStatus().name()
            ))
            .toList();

        // Quando o serviço GetProductService é chamado, ele retorna a lista de produtos
        when(getProductService.execute(any())).thenReturn(productsDTO);

        // Então, executamos a requisição GET para buscar todos os produtos
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk()) // Espera-se um código 200
                .andExpect(jsonPath("$.products.size()").value(2)) // Espera-se que haja 2 produtos
                .andExpect(jsonPath("$.products[0].name").value("Produto Teste")) // Verifica o nome do primeiro produto
                .andExpect(jsonPath("$.products[1].name").value("Produto Teste 2")); // Verifica o nome do segundo produto
        
        // Verifica que o serviço foi chamado uma vez
        verify(getProductService, times(1)).execute(any());
    }

    @Test
    void TestGetProductById_NotFound() throws Exception {
        // Quando o serviço GetProductIdService é chamado, ele lança uma exceção ProductNotFoundException
        when(getProductIdService.execute(anyLong())).thenThrow(new ProductNotFoundException("Produto não encontrado"));

        // Então, executamos a requisição GET para buscar o produto
        mockMvc.perform(get("/products/{id}", 999L))
                .andExpect(status().isNotFound()) // Espera-se um código 404
                .andExpect(jsonPath("$." + Constants.MESSAGE).value("Produto não encontrado")); // Verifica a mensagem de erro
        
        // Verifica que o serviço foi chamado uma vez com o ID do produto
        verify(getProductIdService, times(1)).execute((999L));
    }

    @Test
    void getExpiringProductsTest() throws Exception {
        //Mockando comportamento do service
        when(getExpiringProductsService.execute()).thenReturn("2");

        //Realizando requisição GET
        mockMvc.perform(get("/products/expiring"))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$.expiring_products").value("2"));
        
        //Verificando chamada correta do service
        verify(getExpiringProductsService, times(1)).execute();
    }

    final Long productId = 1L;

    @Test
    void changeProductStatusDonatedTest() throws Exception {
        //Mockando comportamento do service
        doNothing().when(setProductStatusService).execute(productId, ProductStatus.DONATED);

        //Executando controller
        ResponseEntity<Map<String, String>> response = productController.setProductDonated(productId);

        //Verificando
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Status modificado com sucesso!", response.getBody().get(Constants.MESSAGE));
    }

    @Test
    void changeProductStatusDonatedFail1Test() throws Exception { 
        //Mockando comportamento do service
        doThrow(new ProductNotFoundException("Produto não encontrado!")).when(setProductStatusService).execute(productId, ProductStatus.DONATED);

        //Executando controller
        ResponseEntity<Map<String, String>> response = productController.setProductDonated(productId);
        
        //Verificando
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Produto não encontrado!", response.getBody().get(Constants.MESSAGE));
    }
    
    @Test
    void changeProductStatusDonatedFail2Test() throws Exception {
        //Mockando comportamento do service
        doThrow(new ProductNotAvailableException("Produto não disponível")).when(setProductStatusService).execute(productId, ProductStatus.DONATED);

        //Executando controller
        ResponseEntity<Map<String, String>> response = productController.setProductDonated(productId);

        //Verificando
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Produto não disponível", response.getBody().get(Constants.MESSAGE));
    }

    @Test
    void changeProductStatusDonatedFail3Test() throws Exception {
        //Mockando comportamento do service
        doThrow(new RuntimeException("Erro interno")).when(setProductStatusService).execute(productId, ProductStatus.DONATED);

        //Executando controller
        ResponseEntity<Map<String, String>> response = productController.setProductDonated(productId);

        //Verificando
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(Constants.GENERALEXCEPTIONCATCHTEXT + "Erro interno", response.getBody().get(Constants.MESSAGE));
    }

    @Test
    void changeProductStatusDiscardedTest() throws Exception {
        //Mockando comportamento do service
        doNothing().when(setProductStatusService).execute(productId, ProductStatus.DISCARDED);

        //Executando controller
        ResponseEntity<Map<String, String>> response = productController.setProductDiscarded(productId);

        //Verificando
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Status modificado com sucesso!", response.getBody().get(Constants.MESSAGE));
    }

    @Test
    void changeProductStatusDiscardedFail1Test() throws Exception { 
        //Mockando comportamento do service
        doThrow(new ProductNotFoundException("Produto não encontrado!")).when(setProductStatusService).execute(productId, ProductStatus.DISCARDED);

        //Executando controller
        ResponseEntity<Map<String, String>> response = productController.setProductDiscarded(productId);
        
        //Verificando
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Produto não encontrado!", response.getBody().get(Constants.MESSAGE));
    }
    
    @Test
    void changeProductStatusDiscardedFail2Test() throws Exception {
        //Mockando comportamento do service
        doThrow(new ProductNotAvailableException("Produto não disponível")).when(setProductStatusService).execute(productId, ProductStatus.DISCARDED);

        //Executando controller
        ResponseEntity<Map<String, String>> response = productController.setProductDiscarded(productId);

        //Verificando
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Produto não disponível", response.getBody().get(Constants.MESSAGE));
    }

    @Test
    void changeProductStatusDiscardedFail3Test() throws Exception {
        //Mockando comportamento do service
        doThrow(new RuntimeException("Erro interno")).when(setProductStatusService).execute(productId, ProductStatus.DISCARDED);

        //Executando controller
        ResponseEntity<Map<String, String>> response = productController.setProductDiscarded(productId);

        //Verificando
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(Constants.GENERALEXCEPTIONCATCHTEXT + "Erro interno", response.getBody().get(Constants.MESSAGE));
    }
}
