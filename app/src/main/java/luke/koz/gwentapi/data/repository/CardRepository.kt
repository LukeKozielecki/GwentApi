package luke.koz.gwentapi.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import luke.koz.gwentapi.data.local.dao.CardDao
import luke.koz.gwentapi.data.local.entity.CardEntity
import luke.koz.gwentapi.data.local.entity.toDomain
import luke.koz.gwentapi.data.local.entity.toEntity
import luke.koz.gwentapi.data.remote.api.ApiService
import luke.koz.gwentapi.data.remote.model.CardDto
import luke.koz.gwentapi.domain.model.CardGalleryEntry
import java.io.IOException

class CardRepository(
    private val api: ApiService,
    private val dao: CardDao
) {
    fun getCard(cardId: Int): Flow<CardGalleryEntry> = flow {
        val cached = dao.getCardById(cardId).firstOrNull()
        // Emit cached data first if available
        getCachedCard(cardId, cached).collect { emit(it) }

        // Fetch from API and update DB
        if (cached == null) {
            try {
                Log.d("HarassApi","Data was requested from remote source")
                fetchCardFromApiAndUpdateDb(cardId)
            } catch (e: Exception) {
                Log.e("RepoDebug", "API error", e)
                throw e
            }
        }

        // Emit updated data from DB
        getUpdatedCard(cardId).collect { emit(it) }
    }.flowOn(Dispatchers.IO)

    fun getAllCards(): Flow<List<CardGalleryEntry>> = flow {
        // Emit cached data first if available
//        val initialCached = getAllCachedCards()
//        emit(initialCached)
        val cached = getAllCachedCards()
        emit(cached)

        // Fetch from API and update DB
        // todo(chore): implement forceRefresh: Boolean = false as parameter
        if (/*forceRefresh || */cached.isEmpty()) {
            try {
                Log.d("HarassApi","Data was requested from remote source")
                fetchAllCardsFromApiAndUpdateDb()
            } catch (e: Exception) {
                Log.e("RepoDebug", "API error", e)
                throw e
            }
        }
        // Emit updated data from DB
        getAllUpdatedCards().collect { emit(it) }
    }.flowOn(Dispatchers.IO)

    private fun getCachedCard(cardId: Int, cached: CardEntity?): Flow<CardGalleryEntry> = flow {
        Log.d("HarassApi","Data was requested cache")
//        val cached = dao.getCardById(cardId).firstOrNull()
        cached?.let { emit(it.toDomain()) }
    }

    private suspend fun fetchCardFromApiAndUpdateDb(cardId: Int) {
        try {
            val cardDto = fetchCardFromApi(cardId)
            upsertCard(cardDto)
        } catch (e: Exception) {
            Log.e("RepoDebug", "Failed to fetch/update all cards", e)
            // todo: communicate to user that card was not found, gracefully
        }
    }
    private fun fetchCardFromApi(cardId: Int): CardDto {
        val response = api.getCardById(cardId = cardId).execute()
        if (!response.isSuccessful) {
            throw IOException("API error: ${response.code()}")
        }
        return response.body()?.response?.values?.firstOrNull()
            ?: throw Exception("Card $cardId not found")
    }
    private suspend fun upsertCard(cardDto: CardDto) {
        dao.upsertCard(cardDto.toEntity())
    }

    private fun getUpdatedCard(cardId: Int): Flow<CardGalleryEntry> = flow {
        val updated = dao.getCardById(cardId).firstOrNull()
        updated?.let { emit(it.toDomain()) }
    }

    private suspend fun getAllCachedCards(): List<CardGalleryEntry> {
        Log.d("HarassApi","Data was requested cache")
        return dao.getAllCards().first().map { it.toDomain() }
    }

    private suspend fun fetchAllCardsFromApiAndUpdateDb() {
        try {
            val cards = fetchAllCardsFromApi()
            dao.upsertCards(cards.map { it.toEntity() })
        } catch (e: Exception) {
            Log.e("RepoDebug", "Failed to fetch/update all cards", e)
            // todo: communicate to user that card was not found, gracefully
        }
    }

    private fun fetchAllCardsFromApi(): List<CardDto>{
        val response = api.getAllCards().execute()
        if (!response.isSuccessful) {
            throw IOException("API error: ${response.code()}")
        }
        return response.body()?.response?.values?.toList() ?: emptyList()
    }

    private fun getAllUpdatedCards(): Flow<List<CardGalleryEntry>> = flow {
        dao.getAllCards().collect { cardEntities ->
            val galleryEntries = cardEntities.map { it.toDomain() }
            emit(galleryEntries)
        }
    }
}