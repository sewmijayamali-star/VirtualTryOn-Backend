
package com.ZeRo.data.repository

import com.ZeRo.data.tables.ProductTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

data class ProductData(
    val productId: Int,
    val name: String,
    val description: String,
    val price: String,
    val imageUrl: String,
    val category: String,
    val stockQuantity: Int
)

class ProductRepository {

    fun getAllProducts(): List<ProductData> = transaction {

        ProductTable.selectAll().map { row ->

            ProductData(
                productId = row[ProductTable.id].value,
                name = row[ProductTable.name],
                description = row[ProductTable.description],
                price = row[ProductTable.price].toString(),
                imageUrl = row[ProductTable.imageUrl],
                category = row[ProductTable.category],
                stockQuantity = row[ProductTable.stockQuantity]
            )
        }
    }

    fun getProductById(productId: Int): ProductData? = transaction {

        ProductTable
            .selectAll()
            .where { ProductTable.id eq productId }
            .singleOrNull()
            ?.let { row ->

                ProductData(
                    productId = row[ProductTable.id].value,
                    name = row[ProductTable.name],
                    description = row[ProductTable.description],
                    price = row[ProductTable.price].toString(),
                    imageUrl = row[ProductTable.imageUrl],
                    category = row[ProductTable.category],
                    stockQuantity = row[ProductTable.stockQuantity]
                )
            }
    }
}
