package luke.koz.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import luke.koz.data.datasource.CardLocalDataSource
import luke.koz.data.datasource.CardRemoteDataSource
import luke.koz.data.mapper.toCardDetailsEntry
import luke.koz.data.mapper.toCardEntity
import luke.koz.domain.model.CardDetailsEntry
import luke.koz.domain.model.CardDetailsRepository

class CardDetailsRepositoryImpl(
    private val remote: CardRemoteDataSource,
    private val local: CardLocalDataSource
) : CardDetailsRepository {
    override fun getCardDetails(cardId: Int): Flow<CardDetailsEntry> = flow {
        local.getCardById(cardId).collect { cached ->
            cached?.let { emit(it.toCardDetailsEntry()) }

            if (cached == null) {
                try {
                    Log.d("HarassApi", "Data was requested from remote source")
                    val cardDto = remote.getCardById(cardId)
                    local.upsertCard(cardDto.toCardEntity())
                } catch (e: Exception) {
                    Log.e("CardDetailsRepo", "API error", e)
                    throw e
                }
            }

            local.getCardById(cardId).collect { updated ->
                updated?.let { emit(it.toCardDetailsEntry()) }
            }
        }
    }.flowOn(Dispatchers.IO)
}