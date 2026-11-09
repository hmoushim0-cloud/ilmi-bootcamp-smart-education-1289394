package sa.factory.ilmibootcampsmarte.domain.usecase

import sa.factory.ilmibootcampsmarte.domain.repository.IlmiBootcampRepository
import sa.factory.ilmibootcampsmarte.model.IlmiBootcampItem
import sa.factory.ilmibootcampsmarte.model.IlmiBootcampOrder
import sa.factory.ilmibootcampsmarte.model.IlmiBootcampTelemetry
import kotlinx.coroutines.flow.Flow

class GetCatalogUseCase(private val repository: IlmiBootcampRepository) {
    operator fun invoke(): Flow<List<IlmiBootcampItem>> = repository.catalogFlow
}

class ManageCartUseCase(private val repository: IlmiBootcampRepository) {
    val cart = repository.activeCart
    fun add(item: IlmiBootcampItem) = repository.addToCart(item)
    fun remove(item: IlmiBootcampItem) = repository.removeFromCart(item)
    fun clear() = repository.clearCart()
}

class ProcessCheckoutUseCase(private val repository: IlmiBootcampRepository) {
    suspend operator fun invoke(paymentMethod: String = "Mada Contactless EMV"): IlmiBootcampOrder =
        repository.checkout(paymentMethod)
}

class ObserveHardwareTelemetryUseCase(private val repository: IlmiBootcampRepository) {
    operator fun invoke(): Flow<IlmiBootcampTelemetry> = repository.telemetryFlow
}
