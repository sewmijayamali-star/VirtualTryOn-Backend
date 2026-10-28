package com.ZeRo.data.repository

import com.ZeRo.data.tables.UserTable
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.selectAll

class UserRepository {

    fun createUser(
        name: String,
        email: String,
        passwordHash: String
    ): Int {

        return transaction {
            UserTable.insert {
                it[UserTable.name] = name
                it[UserTable.email] = email
                it[UserTable.passwordHash] = passwordHash
            }[UserTable.id].value
        }
    }

    fun findUserByEmail(email: String): Triple<Int, String, String>? {

        return transaction {

            UserTable
                .selectAll()
                .where { UserTable.email eq email }
                .singleOrNull()
                ?.let {
                    Triple(
                        it[UserTable.id].value,
                        it[UserTable.name],
                        it[UserTable.passwordHash]
                    )
                }
        }
    }
}