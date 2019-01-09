package com.softeam.cms

import org.testcontainers.containers.MySQLContainer
import java.time.Duration

class KMysqlContainer : MySQLContainer<KMysqlContainer>(){

    companion object {
        val mysqlSQLContainer = KMysqlContainer().withDatabaseName("testDb")
                .withUsername("user")
                .withPassword("pwd")
                .withStartupTimeout(Duration.ofSeconds(5))
    }
}

