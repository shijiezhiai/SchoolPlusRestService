package com.websystique.springmvc.configuration;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;


/**
 * Created by yangyma on 11/28/16.
 */
@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = {"com.websystique.springmvc.util"})
public class RedisConfig {

    @Autowired
    Environment environment;

    @Autowired
    GenericObjectPoolConfig jedisConfig;

    @Bean(name = "jedisConfig")
    public GenericObjectPoolConfig jedisConfig() {
        GenericObjectPoolConfig jedisConfig = new GenericObjectPoolConfig();
        jedisConfig.setMaxIdle(
                Integer.parseInt(environment.getProperty("redis.pool.maxIdle")));
        jedisConfig.setMinIdle(
                Integer.parseInt(environment.getProperty("redis.pool.minIdle")));
        jedisConfig.setTestOnBorrow(
                Boolean.parseBoolean(environment.getProperty("redis.pool.testOnBorrow")));
        jedisConfig.setTestOnReturn(
                Boolean.parseBoolean(environment.getProperty("redis.pool.testOnReturn")));
        jedisConfig.setTestWhileIdle(
                Boolean.parseBoolean(environment.getProperty("redis.pool.testWhileIdle")));

        return jedisConfig;
    }

    @Bean(name = "jedisPool")
    public JedisPool jedisPool() {
        JedisPool jedisPool = new JedisPool(
                jedisConfig,
                environment.getProperty("redis.host"),
                Integer.parseInt(environment.getProperty("redis.port")),
                Integer.parseInt(environment.getProperty("redis.timeout")),
                environment.getProperty("redis.password"),
                Integer.parseInt(environment.getProperty("redis.database")));


        return jedisPool;
    }

    public Integer getRedisDatabase() {
//        return redisDatabase;
        return Integer.parseInt(environment.getProperty("redis.database"));
    }
}
