package org.example.spring_redis.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("product")  // redis key: product
public class Product {
    @Id private Integer id;             // @Id is used to persist to redis key:id
    private String name;
    private Integer qty;
    private Long price;
}
