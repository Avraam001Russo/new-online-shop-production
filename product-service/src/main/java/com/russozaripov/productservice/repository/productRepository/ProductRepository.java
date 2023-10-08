package com.russozaripov.productservice.repository.productRepository;

import com.russozaripov.productservice.entity.Brand;
import com.russozaripov.productservice.entity.Product;
import com.russozaripov.productservice.entity.Type;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findProductBySkuCode(String skuCode);

    List<Product> findProductsByBrand(Brand brand);
    List<Product> findProductsByType(Type type);
    List<Product> findProductsByTypeAndBrand(Type type, Brand brand);
    @Query("select prod from Product prod where prod.type = :type and prod.brand = :brand and prod.details.price > :minPrice")
    List<Product> findProductsByType_AndBrand_AndPriceGreaterThan(
            @Param("type") Type type,
            @Param("brand") Brand brand,
            @Param("minPrice") int minPrice
    );
    @Query("select prod from Product prod where prod.type = :type and prod.brand = :brand and prod.details.price > :minPrice and prod.details.price < :maxPrice")
    List<Product> findProductsByType_AndBrand_AndPriceGreaterThan_AndPriceLessThen(
            @Param("type") Type type,
            @Param("brand") Brand brand,
            @Param("minPrice") int minPrice,
            @Param("maxPrice") int maxPrice
    );
    @Query("select prod from Product prod where prod.details.price > :minPrice")
    List<Product> findProductsByPriceGreaterThan(@Param("minPrice")int minPrice);

    @Query("select prod from Product prod where prod.details.price < :maxPrice")
    List<Product> findProductsByPriceLessThan(@Param("maxPrice") int maxPrice);


    List<Product> findAll(Specification<Product> specification);

}
