package luke.koz.domain.model

/**
 * A sealed class representing the result of an asynchronous authentication operation.
 * This class provides a structured way to communicate the different states
 * (success, error, loading) and their associated data or messages, making it
 * suitable for propagating results from use cases to ViewModels.
 *
 * @param T The type of data that the operation result holds upon finished operation.
 * @property data The data payload associated with the operation result. It is present for [Success]
 * and optionally for [Error], if partial data is available on error.
 * @property message An optional message providing more details, typically used in [Error] states.
 */
sealed class AuthOperationResult<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : AuthOperationResult<T>(data)
    class Error<T>(message: String, data: T? = null) : AuthOperationResult<T>(data, message)
    class Loading<T> : AuthOperationResult<T>()
}