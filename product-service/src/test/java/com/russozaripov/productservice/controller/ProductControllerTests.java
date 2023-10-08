package com.russozaripov.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.russozaripov.productservice.DTO.ProductDTO;
import com.russozaripov.productservice.exceptionHandler.noSuchProduct.NoSuchProductException;
import com.russozaripov.productservice.service.productService.ProductService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProductService productService;

    @DisplayName("Junit test for save product operation.")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnSavedEmployee() throws Exception {
        ProductDTO productDTO = ProductDTO.builder()
                        .skuCode("sku-code")
                                .title("title")
                                        .isInStock(true)
                                                .productType("product type")
                                                        .productBrand("product brand")
                                                                .productId(1)
                                                                        .description("description")
                                                                                .price(12345)
                                                                                        .localTime(LocalTime.now())
                                                                                                .build();
        String expectedResponse = "successfully";
        BDDMockito.given(productService.add_MetaData_Product(any(ProductDTO.class))).willReturn(expectedResponse);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/product/addProduct/metaData")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO))
        );
        resultActions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", CoreMatchers.is("successfully")))
        ;

    }

    @DisplayName("Junit test for upload photo operation.")
    @Test
    public void givenMultipartFile_whenAddNewPhotoForProduct_thenReturnUrlPhoto() throws Exception {
        byte[] fileToBytes = new byte[4096];
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-originalFileName.jpg",
                "image/jpeg",
                fileToBytes
        );
        BDDMockito.given(productService.add_New_Product(file)).willReturn("www.any-url.com");
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/product/addProduct")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA)
        );
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
    @DisplayName("Junit test for get all products operation.")
    @Test
    public void givenListOfProducts_whenGetAllProducts_thenReturnListOfProducts() throws Exception {
        ProductDTO productDTO_1 = ProductDTO.builder()
                .skuCode("sku-code")
                .title("title")
                .isInStock(true)
                .productType("product type")
                .productBrand("product brand")
                .productId(1)
                .description("description")
                .price(12345)
                .localTime(LocalTime.now())
                .build();
        ProductDTO productDTO_2 = ProductDTO.builder()
                .skuCode("sku-code-2")
                .title("title-2")
                .isInStock(true)
                .productType("product type-2")
                .productBrand("product brand-2")
                .productId(2)
                .description("description-2")
                .price(123456789)
                .localTime(LocalTime.now())
                .build();
        List<ProductDTO> listOfAllProducts = new ArrayList<>(List.of(productDTO_1, productDTO_2));
        CompletableFuture<List<ProductDTO>> future = CompletableFuture.completedFuture(listOfAllProducts);
        BDDMockito.given(productService.get_Products_Is_In_Stock()).willReturn(future);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/product/getAllProducts")
                .contentType(MediaType.APPLICATION_JSON)
        );
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(2)));
    }
    @DisplayName("Junit test for get single product operation.")
    @Test
    public void givenProductObject_whenGetSingleProduct_thenReturnProductObject() throws Exception {
        ProductDTO productDTO_1 = ProductDTO.builder()
                .skuCode("sku-code")
                .title("title")
                .isInStock(true)
                .productType("product type")
                .productBrand("product brand")
                .productId(1)
                .description("description")
                .price(12345)
                .localTime(LocalTime.now())
                .build();
        int productID = 1;
        BDDMockito.given(productService.get_Single_Product(productID)).willReturn(productDTO_1);
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/product/getSingleProduct/{productID}", productID)
                .contentType(MediaType.APPLICATION_JSON)
        );
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is("title")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.skuCode", CoreMatchers.is("sku-code")));
    }

    @DisplayName("Junit test for get single product operation. (Negative scenario)")
    @Test
    public void givenProductID_whenGetSingleProduct_thenReturnNotFoundException() throws Exception {
        int productID = 1;
        BDDMockito.given(productService.get_Single_Product(productID))
                .willThrow(new NoSuchProductException("Product with id: %s not found.".formatted(productID)));
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/product/getSingleProduct/{productID}", productID)
                .contentType(MediaType.APPLICATION_JSON)
        );
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @DisplayName("Junit test for delete product operation.")
    @Test
    public void givenProductID_whenDeleteProductId_thenReturnResponseEntityWithString() throws Exception {
        int productID = 1;
        String resultFromService = "Success.";
        BDDMockito.given(productService.deleteProductById(productID)).willReturn(resultFromService);
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/product/deleteProduct/{productID}", productID)
                .contentType(MediaType.APPLICATION_JSON)
        );
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", CoreMatchers.is(resultFromService)));
    }
    @DisplayName("Junit test for delete product operation. (Negative scenario)")
    @Test
    public void givenProductID_whenDeleteProduct_thenReturnNotFoundException() throws Exception {
        int productID = 1;
        BDDMockito.given(productService.deleteProductById(productID))
                .willThrow(new NoSuchProductException("Product with id: %s not found.".formatted(productID)));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/product/deleteProduct/{productID}", productID)
                .contentType(MediaType.APPLICATION_JSON)
        );
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }
}
