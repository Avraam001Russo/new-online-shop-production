package com.russozaripov.productservice.service.updateProductInStock;

import com.russozaripov.productservice.DTO.ProductDTO;
import com.russozaripov.productservice.DTO.product_to_productDTO.FromProductToProductDTO;
import com.russozaripov.productservice.entity.Product;
import com.russozaripov.productservice.repository.productRepository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class AllProductsIsInStockService {
    public final ProductRepository productRepository;
    public final FromProductToProductDTO fromProductToProductDTO;

    public Callable<List<ProductDTO>> GetAllProductsIsInStockService() {
        return () -> {
        List<Product> productsList = productRepository.findAll();
        log.info("ThreadPoolProduct start work");
        Iterator<Product> productIterator = productsList.iterator();
        List<Product> products_Is_In_Stock = new ArrayList<>();
        while (productIterator.hasNext()){
            Product product = productIterator.next();
            if (product.isInStock()){
                products_Is_In_Stock.add(product);
            }
        }
        List<ProductDTO>productDTOList = products_Is_In_Stock.stream().map(product ->
                fromProductToProductDTO.productToProductDTO(product)
        ).collect(Collectors.toList());
        log.info(productDTOList.toString());
        return productDTOList;
        };
    }
}
