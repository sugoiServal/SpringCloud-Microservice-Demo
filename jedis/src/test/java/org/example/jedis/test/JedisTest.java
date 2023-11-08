package org.example.jedis.test;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.JedisPooled;

public class JedisTest {
    private JedisPooled jedis;
    @BeforeEach
    void setUp() {
         this.jedis = new JedisPooled(new GenericObjectPoolConfig(), "localhost", 6379, 1000, "123321");
    }
    @Test
    void testJedis() {
        jedis.set("clientName", "Jedis");
        String client = jedis.get("clientName");
        System.out.println(client);
    }

    @AfterEach
    void cleanUp() {
        if (jedis != null) {
            jedis.close();
        }
    }
}
