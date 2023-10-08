package com.russozaripov.productservice.service;

import com.russozaripov.productservice.DTO.ProductDTO;
import com.russozaripov.productservice.DTO.product_to_productDTO.FromProductToProductDTO;
import com.russozaripov.productservice.entity.Brand;
import com.russozaripov.productservice.entity.Details;
import com.russozaripov.productservice.entity.Product;
import com.russozaripov.productservice.entity.Type;
import com.russozaripov.productservice.repository.productRepository.ProductRepository;
import com.russozaripov.productservice.service.updateProductInStock.AllProductsIsInStockService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@ExtendWith(MockitoExtension.class)
public class AllProductsIsInStockServiceTests {
    @Mock
    public ProductRepository productRepository;
    @Mock
    public FromProductToProductDTO fromProductToProductDTO;
    @InjectMocks
    private AllProductsIsInStockService allProductsIsInStockService;

    @DisplayName("Junit test for get All products is in stock operation.")
    @Test
    public void givenListProductDTO_whenGetAllProductsIsInStock_thenReturnCallableWithListOfProductDTO() throws Exception {
             Product product_1 = Product.builder()
                .id(1)
                .skuCode("AppleIphone15Pro")
                .title("Apple Iphone 15 Pro")
                .photoUrl("www.s3service.com/any-photo-url")
                .isInStock(true)
                .type(new Type("MobilePhone"))
                .brand(new Brand("Samsung"))
                .details(new Details(165990, "any description"))
                .build();

        Product product_2 = Product.builder()
                .id(2)
                .skuCode("SamsungGalaxys22")
                .title("Samsung Galaxy s 22")
                .photoUrl("www.s3service.com/any-photo-url")
                .isInStock(false)
                .type(new Type("MobilePhone"))
                .brand(new Brand("Samsung"))
                .details(new Details(165990, "any description"))
                .build();

        Product product_3 = Product.builder()
                .id(3)
                .skuCode("Huawei30PROC")
                .title("Huawei 30 PRO C")
                .photoUrl("www.s3service.com/any-photo-url")
                .isInStock(true)
                .type(new Type("MobilePhone"))
                .brand(new Brand("Huawei"))
                .details(new Details(165990, "any description"))
                .build();
        List<Product> listOfProducts = new ArrayList<>(List.of(product_1, product_2, product_3));
        BDDMockito.given(productRepository.findAll()).willReturn(listOfProducts);
        BDDMockito.given(fromProductToProductDTO.productToProductDTO(ArgumentMatchers.any(Product.class)))
                .willAnswer((invocation) -> {
                    Product product = invocation.getArgument(0);
                    return ProductDTO.builder()
                            .skuCode(product.getSkuCode())
                            .title(product.getTitle())
                            .isInStock(product.isInStock())
                            .productType(product.getType().getName())
                            .productBrand(product.getBrand().getName())
                            .productId(product.getId())
                            .description(product.getDetails().getDescription())
                            .price(product.getDetails().getPrice())
                            .localTime(LocalTime.now())
                            .build();

                });
        Callable<List<ProductDTO>> callable = allProductsIsInStockService.GetAllProductsIsInStockService();
        List<ProductDTO> productDTOList = callable.call();
        Assertions.assertThat(productDTOList).isNotNull();
        Assertions.assertThat(productDTOList.size()).isEqualTo(2);
        Assertions.assertThat(productDTOList.stream().allMatch(productDTO -> productDTO.isInStock()));
    }
}
