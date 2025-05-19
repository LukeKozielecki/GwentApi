package luke.koz.gwentapi.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import luke.koz.gwentapi.data.datasource.CardLocalDataSource
import luke.koz.gwentapi.data.datasource.CardRemoteDataSource
import luke.koz.gwentapi.data.mapper.toCardGalleryEntry
import luke.koz.gwentapi.data.mapper.toCardEntity
import luke.koz.gwentapi.domain.model.CardGalleryEntry

class CardGalleryRepository(
    private val remote: CardRemoteDataSource,
    private val local: CardLocalDataSource
) {
    fun getCard(cardId: Int): Flow<CardGalleryEntry> = flow {
        local.getCardById(cardId).collect { cached ->
            cached?.let { emit(it.toCardGalleryEntry()) }

            if (cached == null) {
                try {
                    Log.d("HarassApi", "Data was requested from remote source")
                    val cardDto = remote.getCardById(cardId)
                    local.upsertCard(cardDto.toCardEntity())
                } catch (e: Exception) {
                    Log.e("RepoDebug", "API error", e)
                    throw e
                }
            }

            local.getCardById(cardId).collect { updated ->
                updated?.let { emit(it.toCardGalleryEntry()) }
            }
        }
    }.flowOn(Dispatchers.IO)

    fun getCardByQuery(query: String) : Flow<List<CardGalleryEntry>> = flow {
        local.getCardByQuery(query).collect { cachedList ->
            val domainList = cachedList.map { it.toCardGalleryEntry() }
            emit(domainList)
        }
    }.flowOn(Dispatchers.IO)

    fun getAllCards(forceRefresh: Boolean = false): Flow<List<CardGalleryEntry>> = flow {
        val cached = local.getAllCards().first().map { it.toCardGalleryEntry() }
        emit(cached)

        if (forceRefresh || cached.isEmpty()) {
            try {
                Log.d("HarassApi", "Data was requested from remote source")
                val cardDtoList = remote.getAllCards()
                local.upsertCards(cardDtoList.map { it.toCardEntity() })
            } catch (e: Exception) {
                Log.e("RepoDebug", "Failed to fetch/update all cards", e)
                throw e
            }
        }

        local.getAllCards().collect { entities ->
            emit(entities.map { it.toCardGalleryEntry() })
        }
    }.flowOn(Dispatchers.IO)
}