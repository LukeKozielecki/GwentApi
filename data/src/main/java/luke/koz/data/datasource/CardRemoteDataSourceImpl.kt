package luke.koz.data.datasource

import luke.koz.data.remote.api.ApiService
import luke.koz.data.remote.model.CardDto
import java.io.IOException

class CardRemoteDataSourceImpl(private val api: ApiService) : CardRemoteDataSource {
    override fun getCardById(cardId: Int): CardDto {
        val response = api.getCardById(cardId = cardId).execute()
        if (!response.isSuccessful) throw IOException("API error: ${response.code()}")
        return response.body()?.response?.values?.firstOrNull()
            ?: throw Exception("Card $cardId not found")
    }

    override fun getAllCards(): List<CardDto> {
        val response = api.getAllCards().execute()
        if (!response.isSuccessful) throw IOException("API error: ${response.code()}")
        return response.body()?.response?.values?.toList() ?: emptyList()
    }
}