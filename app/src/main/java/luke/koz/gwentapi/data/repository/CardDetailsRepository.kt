package luke.koz.gwentapi.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import luke.koz.gwentapi.data.datasource.CardLocalDataSource
import luke.koz.gwentapi.data.datasource.CardRemoteDataSource
import luke.koz.gwentapi.data.mapper.toCardDetailsEntry
import luke.koz.gwentapi.data.mapper.toEntity
import luke.koz.gwentapi.domain.model.CardDetailsEntry

class CardDetailsRepository(
    private val remote: CardRemoteDataSource,
    private val local: CardLocalDataSource
) {
    fun getCardDetails(cardId: Int): Flow<CardDetailsEntry> = flow {
        local.getCardById(cardId).collect { cached ->
            cached?.let { emit(it.toCardDetailsEntry()) }

            if (cached == null) {
                try {
                    Log.d("HarassApi", "Data was requested from remote source")
                    val cardDto = remote.getCardById(cardId)
                    local.upsertCard(cardDto.toEntity())
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