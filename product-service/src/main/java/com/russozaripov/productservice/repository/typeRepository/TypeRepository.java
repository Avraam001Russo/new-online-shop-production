package com.russozaripov.productservice.repository.typeRepository;

import com.russozaripov.productservice.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TypeRepository extends JpaRepository<Type, Integer> {
    Optional<Type> findTypeByName(String name);
}
