package luke.koz.data.datasource

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import luke.koz.domain.model.UserLikesDataSource
import java.io.IOException

private const val USER_LIKES_NODE = "userLikes"
private const val LOG_TAG_FIREBASE = "FirebaseUserLikesDS"
private const val LOG_TAG_AUTH = "FirebaseAuth"

class FirebaseUserLikesDataSource(
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseAuth: FirebaseAuth
) : UserLikesDataSource {
    private val userLikesRef: DatabaseReference = firebaseDatabase
        .reference
        .child(USER_LIKES_NODE)

    companion object {
        private const val LIKES_CHILD_NODE = "likes"
        private const val USERS_CHILD_NODE = "users"
    }

    //todo fix: when user turns off wifi, toggles like, and connect wifi it will emit the change and add like no matter what
    override suspend fun toggleCardLike(userId: String, cardId: Int, isLiking: Boolean) {
        Log.d(LOG_TAG_AUTH, firebaseAuth.currentUser?.uid.toString())

        val cardRef = userLikesRef.child(cardId.toString())
        val likesCounterRef = cardRef.child(LIKES_CHILD_NODE)
        val userLikeRef = cardRef.child(USERS_CHILD_NODE).child(userId)

        try {
            val updates = hashMapOf<String, Any?>(
                "${USERS_CHILD_NODE}/$userId" to isLiking.takeIf { it },
                LIKES_CHILD_NODE to ServerValue.increment(if (isLiking) 1 else -1)
            )
            cardRef.updateChildren(updates).await()
            Log.d(LOG_TAG_FIREBASE, "Toggle successful for card $cardId, isLiking: $isLiking")
        } catch (e: Exception) {
            Log.e(LOG_TAG_FIREBASE, "Toggle failed for card $cardId", e)
            throw IOException("Failed to toggle like for card $cardId", e)
        }
    }

    override fun observeLikesForAllCards(): Flow<Map<Int, Set<String>>> = callbackFlow {
        val listener = userLikesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val likesMap = mutableMapOf<Int, Set<String>>()
                for (cardSnap in snapshot.children) {
                    val cardId = cardSnap.key?.toIntOrNull()
                    if (cardId != null) {
                        val users = cardSnap.child(USERS_CHILD_NODE).children.mapNotNull { userSnap ->
                            userSnap.key.takeIf { userSnap.getValue(Boolean::class.java) == true }
                        }.toSet()
                        likesMap[cardId] = users
                    } else {
                        Log.w(LOG_TAG_FIREBASE, "Skipping card with non-integer key: ${cardSnap.key}")
                    }
                }
                trySend(likesMap)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(LOG_TAG_FIREBASE, "Likes observation cancelled", error.toException())
                close(error.toException())
            }
        })
        awaitClose { userLikesRef.removeEventListener(listener) }
    }

    override suspend fun getLikedCardIdsForUser(userId: String): Set<Int> {
        Log.d(LOG_TAG_FIREBASE, "Attempting to fetch liked cards for user: $userId")
        return try {
            val snapshot = userLikesRef.get().await()

            val likedCardIds = mutableSetOf<Int>()
            for (cardSnapshot in snapshot.children) {
                val cardId = cardSnapshot.key?.toIntOrNull()
                if (cardId != null) {
                    val userLiked = cardSnapshot.child(USERS_CHILD_NODE).child(userId).getValue(Boolean::class.java)
                    if (userLiked == true) {
                        likedCardIds.add(cardId)
                    }
                } else {
                    Log.w(LOG_TAG_FIREBASE, "Skipping card with non-integer key during user like fetch: ${cardSnapshot.key}")
                }
            }
            Log.d(LOG_TAG_FIREBASE, "Fetched liked card IDs for user $userId: $likedCardIds")
            likedCardIds
        } catch (e: Exception) {
            Log.e(LOG_TAG_FIREBASE, "Failed to fetch liked cards for user $userId", e)
            throw IOException("Failed to fetch liked cards for user $userId", e)
        }
    }

    override suspend fun getLikesForAllCards(): Map<Int, Set<String>> {
        Log.d(LOG_TAG_FIREBASE, "Attempting to fetch likes for all cards (one-time)")
        return try {
            val snapshot = userLikesRef.get().await()
            val result = mutableMapOf<Int, Set<String>>()

            for (cardSnapshot in snapshot.children) {
                val cardId = cardSnapshot.key?.toIntOrNull()
                if (cardId != null) {
                    val users = cardSnapshot.child(USERS_CHILD_NODE).children.mapNotNull { userSnap ->
                        userSnap.key.takeIf { userSnap.getValue(Boolean::class.java) == true }
                    }.toSet()

                    if (users.isNotEmpty()) {
                        result[cardId] = users
                    }
                } else {
                    Log.w(LOG_TAG_FIREBASE, "Skipping card with non-integer key during all likes fetch: ${cardSnapshot.key}")
                }
            }
            Log.d(LOG_TAG_FIREBASE, "Fetched all likes: ${result.size} cards with likes.")
            result
        } catch (e: Exception) {
            Log.e(LOG_TAG_FIREBASE, "Failed to fetch all likes (one-time)", e)
            throw IOException("Failed to fetch all likes", e)
        }
    }
}