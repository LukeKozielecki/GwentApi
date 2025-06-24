package luke.koz.data.datasource

import luke.koz.data.local.dao.CardDao
import luke.koz.data.local.entity.CardEntity

class CardLocalDataSourceImpl(private val dao: CardDao): CardLocalDataSource {
    override suspend fun upsertCard(card: CardEntity) = dao.upsertCard(card)
    override suspend fun upsertCards(cards: List<CardEntity>) = dao.upsertCards(cards)

    override fun getCardById(cardId: Int) = dao.getCardById(cardId)
    override fun getCardByQuery(query: String) = dao.getCardByQuery(query)
    override fun getAllCards() = dao.getAllCards()
}