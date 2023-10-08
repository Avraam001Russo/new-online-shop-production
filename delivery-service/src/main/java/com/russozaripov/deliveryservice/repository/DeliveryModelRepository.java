package com.russozaripov.deliveryservice.repository;

import com.russozaripov.deliveryservice.model.DeliveryModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryModelRepository extends JpaRepository<DeliveryModel, Integer> {
}
