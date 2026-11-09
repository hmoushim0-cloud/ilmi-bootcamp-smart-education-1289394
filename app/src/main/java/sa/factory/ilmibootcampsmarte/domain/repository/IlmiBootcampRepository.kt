package sa.factory.ilmibootcampsmarte.domain.repository

import sa.factory.ilmibootcampsmarte.model.IlmiBootcampItem
import sa.factory.ilmibootcampsmarte.model.IlmiBootcampOrder
import sa.factory.ilmibootcampsmarte.model.IlmiBootcampTelemetry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface IlmiBootcampRepository {
    val catalogFlow: Flow<List<IlmiBootcampItem>>
    val activeCart: StateFlow<List<IlmiBootcampItem>>
    val telemetryFlow: Flow<IlmiBootcampTelemetry>
    
    fun addToCart(item: IlmiBootcampItem)
    fun removeFromCart(item: IlmiBootcampItem)
    fun clearCart()
    suspend fun checkout(paymentMethod: String): IlmiBootcampOrder
    suspend fun refreshCatalog()
}
