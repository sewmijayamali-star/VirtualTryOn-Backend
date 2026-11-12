
package com.ZeRo.service

import com.ZeRo.data.repository.ProductData
import com.ZeRo.data.repository.ProductRepository

class ProductService(
    private val repository: ProductRepository = ProductRepository()
) {

    fun getAllProducts(): List<ProductData> {
        return repository.getAllProducts()
    }

    fun getProductById(productId: Int): ProductData? {
        return repository.getProductById(productId)
    }
}
