package luke.koz.gwentapi.data.mapper

import com.google.firebase.auth.FirebaseUser
import luke.koz.gwentapi.domain.model.AuthUserModel

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
//fun FirebaseUser.toAuthUserModel(): AuthUserModel {
//    return AuthUserModel(
//        id = uid,
//        email = email ?: ""
//    )
//}