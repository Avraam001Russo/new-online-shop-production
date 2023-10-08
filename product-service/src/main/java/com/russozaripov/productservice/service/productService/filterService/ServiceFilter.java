package com.russozaripov.productservice.service.productService.filterService;

import com.russozaripov.productservice.entity.Brand;
import com.russozaripov.productservice.entity.Product;
import com.russozaripov.productservice.entity.Type;
import com.russozaripov.productservice.exceptionHandler.noSuchProduct.NoSuchBrand.NoSuchBrandException;
import com.russozaripov.productservice.exceptionHandler.noSuchProduct.noSuchType.NoSuchTypeException;
import com.russozaripov.productservice.repository.brandRepository.BrandRepository;
import com.russozaripov.productservice.repository.typeRepository.TypeRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceFilter {
    private final BrandRepository brandRepository;
    private final TypeRepository typeRepository;



    public Specification<Product> filterProductsWithSpecification(String type,
                                                                  String brand,
                                                                  Integer minPrice,
                                                                  Integer maxPrice,
                                                                  boolean isInStock
    ){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (isInStock){
                predicates.add(criteriaBuilder.equal(root.get("isInStock"), true));
            }
            if (type != null){
                Type savedType = typeRepository.findTypeByName(type).orElseThrow(() -> new NoSuchTypeException("Type with name: %s not found.".formatted(type)));
                predicates.add(criteriaBuilder.equal(root.get("type"), savedType));
            }
            if (brand != null){
                Brand savedBrand = brandRepository.findBrandByName(brand).orElseThrow(() -> new NoSuchBrandException("Brand with name: %s not found.".formatted(brand)));
                predicates.add(criteriaBuilder.equal(root.get("brand"), savedBrand));
            }
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("details").get("price"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("details").get("price"), maxPrice));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        });
    }
}
