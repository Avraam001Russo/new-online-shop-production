package com.russozaripov.inventoryservice.repository;


import com.russozaripov.inventoryservice.model.InventoryModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface InventRepository extends MongoRepository<InventoryModel, String> {
    Optional<InventoryModel> findInventoryModelBySkuCode(String skuCode);
}
