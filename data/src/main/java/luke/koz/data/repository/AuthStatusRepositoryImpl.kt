package luke.koz.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import luke.koz.domain.auth.AuthUserModel
import luke.koz.domain.repository.AuthStatusRepository

class AuthStatusRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthStatusRepository {

    override fun observeCurrentUser(): Flow<AuthUserModel?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            val firebaseUser = auth.currentUser
            val authUserModel = firebaseUser?.let {
                AuthUserModel(id = it.uid, email = it.email ?: "")
            }
            trySend(authUserModel)
        }

        firebaseAuth.addAuthStateListener(authStateListener)

        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }
}