package sa.factory.ilmibootcampsmarte.di

import sa.factory.ilmibootcampsmarte.data.repository.IlmiBootcampRepositoryImpl
import sa.factory.ilmibootcampsmarte.domain.repository.IlmiBootcampRepository
import sa.factory.ilmibootcampsmarte.domain.usecase.*
import sa.factory.ilmibootcampsmarte.hardware.HardwareSensorDriver
import sa.factory.ilmibootcampsmarte.ui.viewmodel.IlmiBootcampViewModel

class DiContainer {
    val sensorDriver = HardwareSensorDriver()
    val repository: IlmiBootcampRepository = IlmiBootcampRepositoryImpl(sensorDriver)

    val getCatalogUseCase = GetCatalogUseCase(repository)
    val manageCartUseCase = ManageCartUseCase(repository)
    val processCheckoutUseCase = ProcessCheckoutUseCase(repository)
    val observeHardwareTelemetryUseCase = ObserveHardwareTelemetryUseCase(repository)

    fun provideViewModel(): IlmiBootcampViewModel = IlmiBootcampViewModel(
        getCatalogUseCase,
        manageCartUseCase,
        processCheckoutUseCase,
        observeHardwareTelemetryUseCase
    )
}
