package luke.koz.data.datasource

import luke.koz.data.local.dao.CardDao
import luke.koz.data.local.entity.CardEntity

class CardLocalDataSource(private val dao: CardDao) {
    suspend fun upsertCard(card: CardEntity) = dao.upsertCard(card)
    suspend fun upsertCards(cards: List<CardEntity>) = dao.upsertCards(cards)

    fun getCardById(cardId: Int) = dao.getCardById(cardId)
    fun getCardByQuery(query: String) = dao.getCardByQuery(query)
    fun getAllCards() = dao.getAllCards()
}