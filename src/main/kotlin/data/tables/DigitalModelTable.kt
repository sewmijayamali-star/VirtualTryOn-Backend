
package com.ZeRo.data.tables

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object DigitalModelTable : IntIdTable("digital_models") {

    val userId = reference("user_id", UserTable)
        .uniqueIndex()
    val modelImageUrl = varchar("model_image_url", 500)

    val status = varchar("status", 30)
        .default("PENDING")
}