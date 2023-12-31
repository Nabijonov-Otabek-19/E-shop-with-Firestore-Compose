package uz.gita.androidexam.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.gita.androidexam.data.common.Product
import uz.gita.androidexam.data.common.Products

interface AppRepository {
    fun fetchAllProducts(): Flow<Result<List<Products>>>
    fun fetchProductbyUserId(userId: String): Flow<Result<List<Products>>>
    fun addProduct(product: Product): Flow<Result<Unit>>
    fun addCategory(category: String): Flow<Result<Unit>>
    fun fetchCategories(): Flow<Result<List<String>>>
}