package com.softeam.cms

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import java.sql.SQLException
import javax.sql.DataSource

@TestConfiguration
class TestConfig {

    @Bean
    @Throws(SQLException::class)
    fun dataSource(): DataSource {
        var hikariConfig = HikariConfig()
        KMysqlContainer.mysqlSQLContainer.start().run {
            hikariConfig.jdbcUrl = KMysqlContainer.Companion.mysqlSQLContainer.jdbcUrl
            hikariConfig.username = KMysqlContainer.Companion.mysqlSQLContainer.username
            hikariConfig.password = KMysqlContainer.Companion.mysqlSQLContainer.password
            hikariConfig.isAllowPoolSuspension = true
        }
        return HikariDataSource(hikariConfig)
    }
}

