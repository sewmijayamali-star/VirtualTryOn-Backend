package com.ZeRo.data.tables

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object UserTable : IntIdTable("users") {

    val name = varchar("name", 100)

    val email = varchar("email", 150).uniqueIndex()

    val passwordHash = varchar("password_hash", 255)
}