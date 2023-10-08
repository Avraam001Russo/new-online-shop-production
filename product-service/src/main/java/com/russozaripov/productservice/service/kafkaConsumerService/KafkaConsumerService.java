package com.russozaripov.productservice.service.kafkaConsumerService;

import com.russozaripov.productservice.entity.Product;
import com.russozaripov.productservice.event.Supply_product_DTO;
import com.russozaripov.productservice.repository.productRepository.ProductRepository;
import com.russozaripov.productservice.service.productService.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final ProductRepository productRepository;
    private final ProductService productService;
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerService.class);
    @KafkaListener(topics = "inventory-topic", groupId = "myGroup")
    public void get_Message_From_Inventory(Supply_product_DTO supply_product_dto) throws Exception {
        LOGGER.info("Message received -> %s".formatted(supply_product_dto));
        Product product = productRepository.findProductBySkuCode(supply_product_dto.getSkuCode()).get();
        if (supply_product_dto.getQuantity() > 0){
        product.setInStock(true);
        }
        else {
            product.setInStock(false);
        }
        productRepository.save(product);
        LOGGER.info("Is in stock -> %s".formatted(supply_product_dto));
        productService.update_Cache_With_AllProducts();
    }
}
