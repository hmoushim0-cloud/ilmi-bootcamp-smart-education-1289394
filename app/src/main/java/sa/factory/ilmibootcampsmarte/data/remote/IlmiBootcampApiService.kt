package sa.factory.ilmibootcampsmarte.data.remote

import sa.factory.ilmibootcampsmarte.model.IlmiBootcampItem
import sa.factory.ilmibootcampsmarte.model.IlmiBootcampOrder

interface IlmiBootcampApiService {
    suspend fun fetchCatalog(): List<IlmiBootcampItem>
    suspend fun submitOrder(order: IlmiBootcampOrder): Boolean
}
