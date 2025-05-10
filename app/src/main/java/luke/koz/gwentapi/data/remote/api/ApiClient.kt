package luke.koz.gwentapi.data.remote.api

import luke.koz.gwentapi.data.remote.model.ApiResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("cards")
    fun getCardById(
        @Query("key") key: String = "data",
        @Query("id") cardId: Int,
        @Query("response") response: String = "json"
    ): Call<ApiResponse>
}

object RetrofitClient {
    private const val BASE_URL = "https://api.gwent.one/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

object ApiClient {
    val apiService: ApiService by lazy {
        RetrofitClient.retrofit.create(ApiService::class.java)
    }
}
