package luke.koz.gwentapi.domain.model

sealed class AuthOperationResult<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : AuthOperationResult<T>(data)
    class Error<T>(message: String, data: T? = null) : AuthOperationResult<T>(data, message)
    class Loading<T> : AuthOperationResult<T>()
}