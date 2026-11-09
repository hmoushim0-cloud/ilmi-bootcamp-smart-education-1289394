package sa.factory.ilmibootcampsmarte.data.repository

import sa.factory.ilmibootcampsmarte.domain.repository.IlmiBootcampRepository
import sa.factory.ilmibootcampsmarte.hardware.HardwareSensorDriver
import sa.factory.ilmibootcampsmarte.model.IlmiBootcampItem
import sa.factory.ilmibootcampsmarte.model.IlmiBootcampOrder
import sa.factory.ilmibootcampsmarte.model.IlmiBootcampTelemetry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class IlmiBootcampRepositoryImpl(
    private val sensorDriver: HardwareSensorDriver
) : IlmiBootcampRepository {

    private val _catalog = MutableStateFlow<List<IlmiBootcampItem>>(listOf(
        IlmiBootcampItem("1", "مراجعة دليل السلامة الشامل", "تحليل 99 بنداً من إجراءات السلامة باستخدام قلم رقمي مع شهادة NEBOSH/IOSH", 150.0, "معتمد", "فحص المواد الخطرة"),
        IlmiBootcampItem("2", "تقييم بيئي فوري", "مسح ميداني للمواد الخطرة عبر منارات BLE مع تقرير خلال 60 دقيقة", 280.0, "فوري", "إجراءات السلامة البيئية"),
        IlmiBootcampItem("3", "تدريب تفاعلي على OSHA 30", "دورات معتمدة مع محاكاة واقعية لمواقع التشييد عبر شاشة منقسمة", 95.0, "جديد", "تدريب OSHA")
    ))
    override val catalogFlow: Flow<List<IlmiBootcampItem>> = _catalog.asStateFlow()

    private val _cart = MutableStateFlow<List<IlmiBootcampItem>>(emptyList())
    override val activeCart: StateFlow<List<IlmiBootcampItem>> = _cart.asStateFlow()

    override val telemetryFlow: Flow<IlmiBootcampTelemetry> = sensorDriver.observePeripheralStream()
        .map { frame ->
            IlmiBootcampTelemetry(
                peripheralName = frame.peripheralName,
                status = frame.connectionState,
                liveReading = frame.livePayload,
                batteryPct = 96,
                signalDbm = -54
            )
        }

    override fun addToCart(item: IlmiBootcampItem) {
        val current = _cart.value.toMutableList()
        current.add(item)
        _cart.value = current
    }

    override fun removeFromCart(item: IlmiBootcampItem) {
        val current = _cart.value.toMutableList()
        current.remove(item)
        _cart.value = current
    }

    override fun clearCart() {
        _cart.value = emptyList()
    }

    override suspend fun checkout(paymentMethod: String): IlmiBootcampOrder {
        val currentItems = _cart.value.toList()
        val subtotal = currentItems.sumOf { it.priceSar }
        val vat = subtotal * 0.15
        val total = subtotal + vat
        val order = IlmiBootcampOrder(
            orderId = "ORD-${System.currentTimeMillis() % 100000}",
            items = currentItems,
            subtotalSar = subtotal,
            vatSar = vat,
            totalSar = total,
            paymentMethod = paymentMethod
        )
        clearCart()
        return order
    }

    override suspend fun refreshCatalog() {
        // Offline-first sync logic
    }
}
