package com.ZeRo.data.repository

import com.ZeRo.data.tables.DigitalModelTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

data class DigitalModelData(
    val modelId: Int,
    val userId: Int,
    val modelImageUrl: String,
    val status: String
)

class DigitalModelRepository {

    fun saveOrUpdate(
        userId: Int,
        modelImageUrl: String
    ): DigitalModelData = transaction {

        val existing = DigitalModelTable
            .selectAll()
            .where { DigitalModelTable.userId eq userId }
            .singleOrNull()

        if (existing == null) {

            val modelId = DigitalModelTable.insert {
                it[DigitalModelTable.userId] = userId
                it[DigitalModelTable.modelImageUrl] = modelImageUrl
                it[DigitalModelTable.status] = "PENDING"
            }[DigitalModelTable.id].value

            DigitalModelData(
                modelId = modelId,
                userId = userId,
                modelImageUrl = modelImageUrl,
                status = "PENDING"
            )

        } else {

            DigitalModelTable.update({
                DigitalModelTable.userId eq userId
            }) {
                it[DigitalModelTable.modelImageUrl] = modelImageUrl
                it[DigitalModelTable.status] = "PENDING"
            }

            DigitalModelData(
                existing[DigitalModelTable.id].value,
                userId,
                modelImageUrl,
                "PENDING"
            )
        }
    }

    fun findByUserId(userId: Int): DigitalModelData? =
        transaction {
            DigitalModelTable
                .selectAll()
                .where { DigitalModelTable.userId eq userId }
                .singleOrNull()
                ?.let { row ->
                    DigitalModelData(
                        modelId = row[DigitalModelTable.id].value,
                        userId = row[DigitalModelTable.userId].value,
                        modelImageUrl = row[DigitalModelTable.modelImageUrl],
                        status = row[DigitalModelTable.status]
                    )
                }
        }
}