package com.ZeRo

import com.ZeRo.data.tables.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import com.ZeRo.data.tables.DigitalModelTable
import com.ZeRo.data.tables.ProductTable

object DatabaseFactory {

    fun init() {

        val config = HikariConfig().apply {

            jdbcUrl = "jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:5432/postgres"

            username = "postgres.uottebpsfvmmqjjndupi"

            password = System.getenv("DB_PASSWORD")

            driverClassName = "org.postgresql.Driver"

            maximumPoolSize = 10
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"

            validate()
        }

        val dataSource = HikariDataSource(config)

        Database.connect(dataSource)

        transaction {
            SchemaUtils.create(
                UserTable,
                DigitalModelTable,
                ProductTable
            )
        }
    }
}