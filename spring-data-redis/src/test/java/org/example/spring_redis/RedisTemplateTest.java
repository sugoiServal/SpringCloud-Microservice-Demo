package org.example.spring_redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.spring_redis.configs.RedisConfig;
import org.example.spring_redis.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = RedisConfig.class)
public class RedisTemplateTest {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @BeforeEach
    void setup() {
        redisTemplate.delete("name");
        stringRedisTemplate.delete("name");
        redisTemplate.delete("myStringList");
        redisTemplate.delete("myObjectList");
        redisTemplate.delete("myHash");
        redisTemplate.delete("user");
        stringRedisTemplate.delete("user");

    }
    @Test
    void testString() {
        stringRedisTemplate.opsForValue().set("name", "aa");
        System.out.println(redisTemplate.opsForValue().get("name"));
    }
    @Test
    void testList() {
        stringRedisTemplate.opsForList().leftPush("myStringList", "a");
        stringRedisTemplate.opsForList().leftPush("myStringList", "b");
        redisTemplate.opsForList().leftPush("myObjectList", "a");
        redisTemplate.opsForList().leftPush("myObjectList", "b");
        assertEquals("a", stringRedisTemplate.opsForList().rightPop("myStringList"));
    }
    @Test
    void testHash() {
        stringRedisTemplate.opsForHash().put("myHash", "name", "Kafka");
        stringRedisTemplate.opsForHash().put("myHash", "age", "24");
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries("myHash");
        System.out.println(entries);
    }

    @Test
    void testPojoAutoSerialization() {
        redisTemplate.opsForValue().set("user:11", new User("Sean", 13));
        User user = (User) redisTemplate.opsForValue().get("user:11");
        System.out.println(user);
    }

    @Test
    void testPojoManualSerialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        // manual serialization: POJO->string
        String userJson = mapper.writeValueAsString(new User("Solo", 13));
        stringRedisTemplate.opsForValue().set("user:12", userJson);
        String userString = stringRedisTemplate.opsForValue().get("user:11");
        User user = mapper.readValue(userString, User.class);
        System.out.println(user);
    }
}