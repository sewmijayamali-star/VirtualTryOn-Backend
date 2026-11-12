package com.ZeRo.data.tables

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object ProductTable : IntIdTable("products") {

    val name = varchar("name", 150)

    val description = text("description")

    val price = decimal("price", 10, 2)

    val imageUrl = varchar("image_url", 500)

    val category = varchar("category", 100)

    val stockQuantity = integer("stock_quantity")
        .default(0)
}