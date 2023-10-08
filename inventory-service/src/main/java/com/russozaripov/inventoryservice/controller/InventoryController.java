package com.russozaripov.inventoryservice.controller;

import com.russozaripov.inventoryservice.DTO.Supply_product_DTO;
import com.russozaripov.inventoryservice.service.inventoryService.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@Slf4j
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> create_New_Inventory_Model(@RequestBody String skuCode){
        String result =  inventoryService.create_New_Inventory_Model(skuCode);
        log.info(result);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/supply")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> supply_Product_To_Stock(@RequestBody Supply_product_DTO supply_product_dto){
        String result = inventoryService.supply_Product(supply_product_dto);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(result);
    }
}
