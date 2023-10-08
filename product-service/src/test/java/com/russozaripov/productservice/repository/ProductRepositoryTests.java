package com.russozaripov.productservice.repository;

import com.russozaripov.productservice.entity.Brand;
import com.russozaripov.productservice.entity.Details;
import com.russozaripov.productservice.entity.Product;
import com.russozaripov.productservice.entity.Type;
import com.russozaripov.productservice.repository.productRepository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

@TestPropertySource(locations = "classpath:application-test.properties")
@DataJpaTest
@Slf4j
public class ProductRepositoryTests {
    @Autowired
    private ProductRepository productRepository;
    private Product product;
    @BeforeEach
    public void setUp(){
        product = Product.builder()
                .skuCode("AppleIphone15Pro")
                .title("Apple Iphone 15 Pro")
                .photoUrl("www.s3service.com/any-photo-url")
                .isInStock(false)
                .type(new Type("MobilePhone"))
                .brand(new Brand("Apple"))
                .details(new Details(165990, "any description"))
                .build();
    }

    @DisplayName("Unit test for product save operation.")
    @Test
    public void testGivenProductObject_whenSave_thenReturnProductSaved(){
        Product savedProduct = productRepository.save(product);
        Assertions.assertThat(savedProduct).isNotNull();
        Assertions.assertThat(savedProduct.getId()).isGreaterThan(0);
    }

    @DisplayName("Junit test for get All products operation.")
    @Test
    public void givenListOfProducts_whenFindAllProducts_thenReturnListOfProducts(){
        Product product_2 = Product.builder()
                .skuCode("AppleIphone15Pro")
                .title("Apple Iphone 15 Pro")
                .photoUrl("www.s3service.com/any-photo-url")
                .isInStock(false)
                .type(new Type("MobilePhone"))
                .brand(new Brand("Apple"))
                .details(new Details(165990, "any description"))
                .build();
        List<Product> listOfProducts = List.of(product_2, product);
       List<Product> savedProducts = productRepository.saveAll(listOfProducts);
            Assertions.assertThat(savedProducts).isNotNull();
            Assertions.assertThat(savedProducts.size()).isEqualTo(2);
    }
    @DisplayName("Junit test for get product by id operation. ")
    @Test
    public void givenProductObject_whenFindProductById_thenReturnProductObject(){
        Product savedProduct = productRepository.save(product);
        Product foundProduct = productRepository.findById(savedProduct.getId()).get();

        Assertions.assertThat(foundProduct).isNotNull();
        Assertions.assertThat(foundProduct.getSkuCode()).isEqualTo("AppleIphone15Pro");
        Assertions.assertThat(foundProduct.getId()).isEqualTo(savedProduct.getId());

    }


    @DisplayName("Junit test for delete product operation.")
    @Test
    public void givenProductObject_whenDeleteProductById_thenReturnNothing(){
        Product savedProduct = productRepository.save(product);
        productRepository.deleteById(savedProduct.getId());
        Optional<Product> findProductWhichWasDelete = productRepository.findById(savedProduct.getId());
        Assertions.assertThat(findProductWhichWasDelete).isEmpty();
    }
}
