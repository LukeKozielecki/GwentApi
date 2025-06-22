package luke.koz.data.mapper

import com.google.firebase.auth.FirebaseUser
import luke.koz.domain.model.AuthUserModel

/**
 * Object for FirebaseUser mappers.
 *
 * Note: This uses an explicit mapper (instead of an extension function) to avoid
 * polluting Firebase's types. Other mappers for project-owned classes follow
 * the extension function pattern.
 *
 */
object FirebaseUserMappers {
    fun toAuthUserModel(firebaseUser: FirebaseUser): AuthUserModel {
        return AuthUserModel(
            id = firebaseUser.uid,
            email = firebaseUser.email ?: ""
        )
    }
}