package luke.koz.gwentapi.data.datasource

import luke.koz.gwentapi.data.local.dao.CardDao
import luke.koz.gwentapi.data.local.entity.CardEntity

class CardLocalDataSource(private val dao: CardDao) {
    suspend fun upsertCard(card: CardEntity) = dao.upsertCard(card)
    suspend fun upsertCards(cards: List<CardEntity>) = dao.upsertCards(cards)

    fun getCardById(cardId: Int) = dao.getCardById(cardId)
    fun getAllCards() = dao.getAllCards()
}