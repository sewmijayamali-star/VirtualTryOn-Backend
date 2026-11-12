package com.ZeRo.service

import com.ZeRo.data.repository.DigitalModelData
import com.ZeRo.data.repository.DigitalModelRepository

class DigitalModelService(
    private val repository: DigitalModelRepository = DigitalModelRepository()
) {

    fun saveDigitalModel(
        userId: Int,
        modelBytes: ByteArray,
        modelType: String
    ): DigitalModelData {

        val modelPath = SupabaseStorageService.uploadImage(
            userId,
            "model.jpg",
            modelBytes,
            modelType
        )

        return repository.saveOrUpdate(
            userId = userId,
            modelImageUrl = modelPath
        )
    }

    fun getDigitalModel(userId: Int): DigitalModelData? =
        repository.findByUserId(userId)
}