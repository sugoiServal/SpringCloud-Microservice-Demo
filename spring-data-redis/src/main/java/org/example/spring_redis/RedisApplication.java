package org.example.spring_redis;

import org.example.spring_redis.models.Product;
import org.example.spring_redis.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RedisApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }

    @Autowired
    ProductRepository productRepository;
    @Override
    public void run(String... args) throws Exception {
        Product product = new Product(1, "rice", 12, (long)200);
        productRepository.save(product);
        System.out.println(productRepository.findById(product.getId()));
    }
}
