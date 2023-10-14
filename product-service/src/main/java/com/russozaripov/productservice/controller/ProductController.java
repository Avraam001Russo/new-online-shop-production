package com.russozaripov.productservice.controller;

import com.russozaripov.productservice.DTO.ProductDTO;
import com.russozaripov.productservice.exceptionHandler.ProductServicEexception.AddPhotoException;
import com.russozaripov.productservice.exceptionHandler.ProductServicEexception.DisconnectInventoryException;
import com.russozaripov.productservice.service.productService.ProductService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@Slf4j
public class ProductController {

    @Value("${eureka.instance.instance-id}")
    private String PRODUCT_SERVICE_INSTANCE_ID;
    @Autowired
    private ProductService productService;
    @PostMapping("/addProduct")
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "productservice", fallbackMethod = "addNewProductFallBackMethode")
    public ResponseEntity<?> Add_New_Product(@RequestParam("file")MultipartFile file) throws Exception {
                String result = productService.add_New_Product(file);
                return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
    public ResponseEntity<?> addNewProductFallBackMethode(MultipartFile file, AddPhotoException exception){
        return ResponseEntity.ok("exception -> " + exception.getMessage());
    }
    @PutMapping("/addProduct/metaData")
    @ResponseStatus(HttpStatus.OK)
    @CircuitBreaker(name = "productservice", fallbackMethod = "fallBackMethodeAddMetadataProduct")
    public ResponseEntity<?> add_MetaData_Product(@RequestBody ProductDTO productDTO){
         String result =  productService.add_MetaData_Product(productDTO);
         return new ResponseEntity<>(result, HttpStatus.OK);
        // HttpStatus.OK идет в заголовок и ResponseEntity будет представлен только в виде строки Result
    }
    public ResponseEntity<?> fallBackMethodeAddMetadataProduct(ProductDTO productDTO, DisconnectInventoryException exception){
        return ResponseEntity.ok(exception.getMessage());
    }

    @GetMapping("/getAllProducts")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ProductDTO>> get_All_Products() throws Exception {
        List<ProductDTO> productDTOS = productService.get_Products_Is_In_Stock().get();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(productDTOS);
    }
    @GetMapping("/getSingleProduct/{productID}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> get_Single_Product(@PathVariable("productID") int id){
        ProductDTO productDTO = productService.get_Single_Product(id);
        return ResponseEntity.ok(productDTO);


    }
    @GetMapping("/getProducts/sortWithSpec")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> get_Sorted_Products_With_Specification_JPA(
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "maxPrice", required = false) Integer maxPrice,
            @RequestParam(value = "minPrice", required = false) Integer minPrice,
            @RequestParam(value = "isInStock", required = false, defaultValue = "true") boolean isInStock,
            @RequestParam(value = "sortedByAscPrice", required = false) boolean ascPrice,
            @RequestParam(value = "sortedByDescPrice", required = false) boolean descPrice
    ){
        List<ProductDTO> productDTOList = productService.findAllProductsWithFilter(
                type, brand, minPrice, maxPrice, isInStock, ascPrice, descPrice
        );
        return ResponseEntity.ok(productDTOList);
    }
    @DeleteMapping("/deleteProduct/{productID}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteProductById(@PathVariable("productID")int id){
        String successDelete = productService.deleteProductById(id);
        return ResponseEntity.ok(successDelete);
    }




}
