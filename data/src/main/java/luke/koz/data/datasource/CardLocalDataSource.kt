package luke.koz.data.datasource

import kotlinx.coroutines.flow.Flow
import luke.koz.data.local.entity.CardEntity

interface CardLocalDataSource {
    suspend fun upsertCard(card: CardEntity)
    suspend fun upsertCards(cards: List<CardEntity>)

    fun getCardById(cardId: Int): Flow<CardEntity>
    fun getCardByQuery(query: String): Flow<List<CardEntity>>
    fun getAllCards(): Flow<List<CardEntity>>
}