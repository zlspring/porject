package com.aohui.btcorg

import com.fasterxml.jackson.databind.ObjectMapper
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.codec.JsonJacksonCodec
import org.redisson.config.Config
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableJpaRepositories
@EnableTransactionManagement
class DataSourceConfig {

    @Bean(destroyMethod = "close")
    @ConfigurationProperties(prefix="test.datasource")
    fun dataSource(): HikariDataSource {
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:mysql://rm-p0w4ye3p87o690599.mysql.australia.rds.aliyuncs.com:3306/btcorg?useUnicode=true&serverTimezone=GMT%2b8"
        config.username = "btc_org_admin"
        config.password = "u4@usUAX%7bmZ!AV"
        config.connectionTestQuery = "SELECT 1"
        config.isReadOnly = false
        config.connectionTimeout = 30000
        config.validationTimeout = 10000
        config.validationTimeout = 600000
        config.maximumPoolSize = 25
        config.connectionInitSql = "SELECT 1"
        return HikariDataSource(config)
    }

    val redisPwd = "cni%mjKLlTV4Ree!3bNYkL9L"
    val redisServer="redis://47.103.159.142:8378"

    @Bean(destroyMethod = "shutdown")
    fun redisson(objectMapper: ObjectMapper):RedissonClient{
        val config = Config()
        config.setCodec(JsonJacksonCodec(objectMapper))
                .useSingleServer()
                .setConnectionPoolSize(32)
                .setAddress(redisServer)
                .setPassword(redisPwd)
        return Redisson.create(config)
    }
}