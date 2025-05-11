package luke.koz.gwentapi.data.datasource

import luke.koz.gwentapi.data.remote.api.ApiService
import luke.koz.gwentapi.data.remote.model.CardDto
import java.io.IOException

class CardRemoteDataSource(private val api: ApiService) {
    fun getCardById(cardId: Int): CardDto {
        val response = api.getCardById(cardId = cardId).execute()
        if (!response.isSuccessful) throw IOException("API error: ${response.code()}")
        return response.body()?.response?.values?.firstOrNull()
            ?: throw Exception("Card $cardId not found")
    }

    fun getAllCards(): List<CardDto> {
        val response = api.getAllCards().execute()
        if (!response.isSuccessful) throw IOException("API error: ${response.code()}")
        return response.body()?.response?.values?.toList() ?: emptyList()
    }
}