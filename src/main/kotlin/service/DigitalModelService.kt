
package com.ZeRo.service

import com.ZeRo.data.repository.DigitalModelData
import com.ZeRo.data.repository.DigitalModelRepository

class DigitalModelService(
    private val repository: DigitalModelRepository = DigitalModelRepository()
) {
    fun saveDigitalModel(
        userId: Int,
        frontBytes: ByteArray,
        frontType: String,
        sideBytes: ByteArray,
        sideType: String,
        backBytes: ByteArray,
        backType: String
    ): DigitalModelData {
        val frontPath = SupabaseStorageService.uploadImage(
            userId, "front.jpg", frontBytes, frontType
        )

        val sidePath = SupabaseStorageService.uploadImage(
            userId, "side.jpg", sideBytes, sideType
        )

        val backPath = SupabaseStorageService.uploadImage(
            userId, "back.jpg", backBytes, backType
        )

        return repository.saveOrUpdate(
            userId = userId,
            frontPhotoUrl = frontPath,
            sidePhotoUrl = sidePath,
            backPhotoUrl = backPath
        )
    }

    fun getDigitalModel(userId: Int): DigitalModelData? =
        repository.findByUserId(userId)
}
