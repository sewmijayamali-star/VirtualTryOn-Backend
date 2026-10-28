
package com.ZeRo.data.tables

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import com.ZeRo.data.tables.UserTable

object DigitalModelTable : IntIdTable("digital_models") {

    val userId = reference("user_id", UserTable)
        .uniqueIndex()

    val frontPhotoUrl = varchar("front_photo_url", 500)

    val sidePhotoUrl = varchar("side_photo_url", 500)

    val backPhotoUrl = varchar("back_photo_url", 500)

    val status = varchar("status", 30)
        .default("PENDING")
}
