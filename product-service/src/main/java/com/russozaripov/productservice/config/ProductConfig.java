package com.russozaripov.productservice.config;

import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableCaching
@EnableAsync
@ComponentScan(basePackages = "com.russozaripov.productservice")
public class ProductConfig {

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("allProductsIsInStock");
    }

    @Bean
    public AmazonS3Client amazonS3Client(){
        return new AmazonS3Client();
    }
}
