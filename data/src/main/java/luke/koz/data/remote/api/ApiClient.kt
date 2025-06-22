package luke.koz.data.remote.api

import luke.koz.data.remote.model.ApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("cards")
    fun getCardById(
        @Query("key") key: String = "data",
        @Query("id") cardId: Int,
        @Query("response") response: String = "json"
    ): Call<ApiResponse>

    @GET("cards")
    fun getAllCards(
        @Query("key") key: String = "data",
        @Query("response") response: String = "json",
        @Query("version") version: String = "13.4.0"
    ): Call<ApiResponse>
}
