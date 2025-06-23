package luke.koz.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import luke.koz.data.mapper.FirebaseUserMappers
import luke.koz.domain.auth.AuthOperationResult
import luke.koz.domain.auth.AuthUserModel
import luke.koz.domain.repository.AuthRepository

class AuthRepositoryImpl : AuthRepository {
    override suspend fun signIn(email: String, password: String): AuthOperationResult<AuthUserModel> {
        return try {
            val result = Firebase.auth.signInWithEmailAndPassword(email, password).await()
            result.user?.let { user ->
                AuthOperationResult.Success(FirebaseUserMappers.toAuthUserModel(user))
            } ?: AuthOperationResult.Error("User not found")
        } catch (e: Exception) {
            AuthOperationResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun register(email: String, password: String): AuthOperationResult<AuthUserModel> {
        return try {
            val result = Firebase.auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let { user ->
                AuthOperationResult.Success(FirebaseUserMappers.toAuthUserModel(user))
            } ?: AuthOperationResult.Error("User not found")
        } catch (e: Exception) {
            AuthOperationResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun getCurrentUser(): AuthUserModel? {
        return Firebase.auth.currentUser?.let { FirebaseUserMappers.toAuthUserModel(it) }
    }

    override suspend fun signOut() {
        Firebase.auth.signOut()
    }
}


