package com.russozaripov.productservice.service;

import com.russozaripov.productservice.DTO.ProductDTO;
import com.russozaripov.productservice.DTO.product_to_productDTO.FromProductToProductDTO;
import com.russozaripov.productservice.entity.Brand;
import com.russozaripov.productservice.entity.Details;
import com.russozaripov.productservice.entity.Product;
import com.russozaripov.productservice.entity.Type;
import com.russozaripov.productservice.repository.brandRepository.BrandRepository;
import com.russozaripov.productservice.repository.productRepository.ProductRepository;
import com.russozaripov.productservice.repository.typeRepository.TypeRepository;
import com.russozaripov.productservice.service.productService.ProductService;
import com.russozaripov.productservice.service.productService.filterService.ServiceFilter;
import com.russozaripov.productservice.service.s3service.S3Service;
import com.russozaripov.productservice.service.updateProductInStock.AllProductsIsInStockService;
import jakarta.persistence.criteria.Predicate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {

    @Mock
    private S3Service s3Service;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private TypeRepository typeRepository;
    @Mock
    private BrandRepository brandRepository;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private FromProductToProductDTO fromProductToProductDTO;
    @Mock
    private AllProductsIsInStockService allProductsIsInStockService;
    @Mock
    private ServiceFilter serviceFilter;
    @InjectMocks
    private ProductService productService;



    private ProductDTO productDTO_1;
    private Product product;
    @BeforeEach
    public void setUp(){
        productDTO_1 = ProductDTO.builder()
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

        product = Product.builder()
                .id(1)
                .skuCode("AppleIphone15Pro")
                .title("Apple Iphone 15 Pro")
                .photoUrl("www.s3service.com/any-photo-url")
                .isInStock(false)
                .type(new Type("MobilePhone"))
                .brand(new Brand("Apple"))
                .details(new Details(165990, "any description"))
                .build();

    }

    @DisplayName("Junit test for add new photo operation.")
    @Test
    public void givenProductObjectAndProductPhotoUrl_whenAddNewProduct_thenReturnPhotoURL() throws IOException {
        byte[] mockFile = new byte[4096];
        MockMultipartFile multipartFile = new MockMultipartFile("test-name", "test-fileName.jpg", "image/jpeg", mockFile);
        String urlPhoto = "www.test-url.com";
        BDDMockito.given(s3Service.add_New_File(multipartFile)).willReturn(urlPhoto);
        BDDMockito.given(productRepository.save(ArgumentMatchers.any(Product.class)))
                .willAnswer((invocation) -> {
                    Product savedProduct = invocation.getArgument(0);
                    savedProduct.setId(1);
                    return savedProduct;
                });

        String productID = productService.add_New_Product(multipartFile);
        System.out.println(productID);
        Assertions.assertThat(productID).isNotNull();
        Assertions.assertThat(productID).isEqualTo(String.valueOf(1));
    }
    @DisplayName("Junit test for add metadata of product operation.")
    @Test
    public void givenProductDTOObject_whenAddMetaDataProduct_thenReturnResponseEntity() throws IOException {
        Product product = Product.builder().id(1).photoUrl("www.s3service.com").build();
        BDDMockito.given(productRepository.findById(productDTO_1.getProductId())).willReturn(Optional.of(product));
        BDDMockito.given(typeRepository.findTypeByName(productDTO_1.getProductType())).willReturn(Optional.empty());
        BDDMockito.given(brandRepository.findBrandByName(productDTO_1.getProductBrand())).willReturn(Optional.empty());
        BDDMockito.given(productRepository.save(ArgumentMatchers.any(Product.class)))
                .willAnswer((invocation) -> {
                 Product savedProduct = invocation.getArgument(0);
                 savedProduct.setSkuCode("some sku-code");
                 return savedProduct;
                });
        ResponseEntity<String> fakeResponse = ResponseEntity.ok("Successfully");
        BDDMockito.given(restTemplate.exchange(
                BDDMockito.anyString(),
                BDDMockito.any(HttpMethod.class),
                BDDMockito.any(HttpEntity.class),
                BDDMockito.<Class<String>>any()
        )).willReturn(fakeResponse);
        String response = productService.add_MetaData_Product(productDTO_1);

        Mockito.verify(productRepository, Mockito.times(1)).save(ArgumentMatchers.any(Product.class));
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response).isEqualTo("Successfully");


    }

    @DisplayName("Junit test for get single product by id operation.")
    @Test
    public void givenProductObject_whenGetSingleProductById_thenReturnProductObject() {



        BDDMockito.given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
        BDDMockito.given(fromProductToProductDTO.productToProductDTO(ArgumentMatchers.any(Product.class)))
                .willAnswer((invocation) -> {
                    Product foundByIdProduct = invocation.getArgument(0);
                    return ProductDTO.builder()
                            .skuCode(foundByIdProduct.getSkuCode())
                            .title(foundByIdProduct.getTitle())
                            .isInStock(foundByIdProduct.isInStock())
                            .productType(foundByIdProduct.getType().getName())
                            .productBrand(foundByIdProduct.getBrand().getName())
                            .productId(foundByIdProduct.getId())
                            .description(foundByIdProduct.getDetails().getDescription())
                            .price(foundByIdProduct.getDetails().getPrice())
                            .localTime(LocalTime.now())
                            .build();

                });
        ProductDTO productDTO = productService.get_Single_Product(product.getId());

        Assertions.assertThat(productDTO).isNotNull();
        Assertions.assertThat(productDTO.getTitle()).isEqualTo(product.getTitle());
        Assertions.assertThat(productDTO.getSkuCode()).isEqualTo(product.getSkuCode());

        Mockito.verify(fromProductToProductDTO, Mockito.times(1)).productToProductDTO(ArgumentMatchers.any(Product.class));

    }
    @DisplayName("Junit test for get all products is in stock operation.")
    @Test
    public void givenListProductDTO_whenGetProductsIsInStock_thenReturnCallableListProductDTO() throws Exception {
        ProductDTO productDTO_2 = ProductDTO.builder()
                .skuCode("sku-code-2")
                .title("title-2")
                .isInStock(true)
                .productType("product type-2")
                .productBrand("product brand-2")
                .productId(2)
                .description("description-2")
                .price(79990)
                .localTime(LocalTime.now())
                .build();
        List<ProductDTO> listOfProductDTO = new ArrayList<>(List.of(productDTO_1, productDTO_2));
        Callable<List<ProductDTO>> callable = () -> listOfProductDTO;
        BDDMockito.given(allProductsIsInStockService.GetAllProductsIsInStockService()).willReturn(callable);

        CompletableFuture<List<ProductDTO>> future = productService.get_Products_Is_In_Stock();
        Assertions.assertThat(future.get()).isNotNull();
        Assertions.assertThat(future.get().size()).isEqualTo(2);

    }

    @DisplayName("Junit test for update cache with all products is in stock operation.")
    @Test
    public void givenListProductDTO_whenUpdateProductsIsInStock_thenReturnCallableListProductDTO() throws Exception {
        ProductDTO productDTO_2 = ProductDTO.builder()
                .skuCode("sku-code-2")
                .title("title-2")
                .isInStock(true)
                .productType("product type-2")
                .productBrand("product brand-2")
                .productId(2)
                .description("description-2")
                .price(79990)
                .localTime(LocalTime.now())
                .build();
        List<ProductDTO> listOfProductDTO = new ArrayList<>(List.of(productDTO_1, productDTO_2));
        Callable<List<ProductDTO>> callable = () -> listOfProductDTO;
        BDDMockito.given(allProductsIsInStockService.GetAllProductsIsInStockService()).willReturn(callable);

        CompletableFuture<List<ProductDTO>> future = productService.get_Products_Is_In_Stock();
        Assertions.assertThat(future.get()).isNotNull();
        Assertions.assertThat(future.get().size()).isEqualTo(2);

    }
    @DisplayName("Junit test for delete product operation.")
    @Test
    public void givenProductDTOObject_whenDeleteProduct_thenReturnString() {
       BDDMockito.given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
       BDDMockito.willDoNothing().given(productRepository).deleteById(product.getId());

       String successfullyDelete = productService.deleteProductById(product.getId());
       Mockito.verify(productRepository, Mockito.times(1)).deleteById(ArgumentMatchers.any());
       Assertions.assertThat(successfullyDelete).isNotNull();
       Assertions.assertThat(successfullyDelete).isEqualTo("Success.");

    }
    @DisplayName("Junit test for find products with filters operation.")
    @Test
    public void givenListFiltersProducts_whenFindAllProductsWithFilters_thenReturnListProductDto(){
        Specification<Product> specification = (((root, query, criteriaBuilder) -> criteriaBuilder.and(new Predicate[0])));
        Product product_2 = Product.builder()
                .id(2)
                .skuCode("AppleIphone12Pro")
                .title("Apple Iphone 12 Pro")
                .photoUrl("www.s3service.com/any-photo-url")
                .isInStock(false)
                .type(new Type("MobilePhone"))
                .brand(new Brand("Apple"))
                .details(new Details(165990, "any description"))
                .build();
        List<Product> productList = new ArrayList<>(List.of(product, product_2));
        BDDMockito.given(serviceFilter.filterProductsWithSpecification(
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyBoolean()
                )).willReturn(specification);

        BDDMockito.given(productRepository.findAll(specification)).willReturn(productList);
        BDDMockito.given(fromProductToProductDTO.productToProductDTO(ArgumentMatchers.any(Product.class))).willAnswer((invocation) -> {
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

        List<ProductDTO> productDTOList = productService.findAllProductsWithFilter(
                "anytype", "anybrand", 0, 100, true, false, false
        );
        Assertions.assertThat(productDTOList).isNotNull();
        Assertions.assertThat(productDTOList.size()).isEqualTo(2);

    }
}
