package luke.koz.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import luke.koz.data.local.entity.CardEntity

@Dao
interface CardDao{
    @Query("SELECT * FROM cards WHERE card_id = :cardId")
    fun getCardById(cardId: Int): Flow<CardEntity>

    @Query("SELECT * FROM cards WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%'")
    fun getCardByQuery(query: String): Flow<List<CardEntity>>

    @Query("SELECT * FROM cards")
    fun getAllCards(): Flow<List<CardEntity>>

    @Upsert
    suspend fun upsertCard(card: CardEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCards(cards: List<CardEntity>)
}