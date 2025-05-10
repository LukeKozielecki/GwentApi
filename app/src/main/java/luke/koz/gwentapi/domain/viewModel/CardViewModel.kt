package luke.koz.gwentapi.domain.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import luke.koz.gwentapi.data.remote.api.ApiClient
import luke.koz.gwentapi.data.remote.model.ApiResponse
import luke.koz.gwentapi.data.remote.model.toDomain
import luke.koz.gwentapi.domain.model.CardGalleryEntry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CardViewModel : ViewModel (){
    private val _cardState = mutableStateOf<CardState>(CardState.Empty)
    val cardState: State<CardState> = _cardState

    fun getCardById(cardId: Int) {
        _cardState.value = CardState.Loading

        ApiClient.apiService.getCardById(cardId = cardId).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    val cardDto = apiResponse
                        ?.response
                        ?.values
                        ?.firstOrNull()

                    if (cardDto != null) {
                        _cardState.value = CardState.Success(cardDto.toDomain())
                    } else {
                        _cardState.value = CardState.Error("Card not found")
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                _cardState.value = CardState.Error("Network Error: ${t.message}")
            }
        })
    }
}

sealed class CardState {
    object Empty : CardState()
    object Loading : CardState()
    data class Success(val card: CardGalleryEntry) : CardState()
    data class Error(val message: String) : CardState()
}