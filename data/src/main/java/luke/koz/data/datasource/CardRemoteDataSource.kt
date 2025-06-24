package luke.koz.data.datasource

import luke.koz.data.remote.model.CardDto

interface CardRemoteDataSource {
    fun getCardById(cardId: Int): CardDto
    fun getAllCards(): List<CardDto>
}