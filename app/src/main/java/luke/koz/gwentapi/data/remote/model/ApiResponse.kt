package luke.koz.gwentapi.data.remote.model

// Top-level response structure
data class ApiResponse(
    val request: ApiRequest,
    val response: Map<String, CardDto>
)

data class ApiRequest(
    val message: String,
    val status: Int,
    val REQUEST: RequestDetails
)

data class RequestDetails(
    val id: String,
    val response: String,
    val version: String,
    val language: String
)
