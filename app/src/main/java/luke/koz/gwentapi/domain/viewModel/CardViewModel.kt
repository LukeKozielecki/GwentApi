package luke.koz.gwentapi.domain.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import luke.koz.gwentapi.data.repository.CardRepository
import luke.koz.gwentapi.domain.model.CardGalleryEntry

class CardViewModel (private val repository: CardRepository) : ViewModel (){
    private val _cardState = mutableStateOf<CardState>(CardState.Empty)
    val cardState: State<CardState> = _cardState
    private val _searchState = mutableStateOf<SearchState>(SearchState.Idle)
    val searchState: State<SearchState> = _searchState

    private var searchJob: Job? = null

    fun getCardById(cardId: Int) {
        _cardState.value = CardState.Loading
        viewModelScope.launch {
            repository.getCard(cardId)
                .catch { e ->
                    _cardState.value = CardState.Error("Error: ${e.message}")
                }
                .collect { card ->
                    _cardState.value = CardState.Success(listOf(card))
                }
        }
    }
    fun getAllCards(){
        _cardState.value = CardState.Loading
        viewModelScope.launch {
            repository.getAllCards()
                .catch { e ->
                    _cardState.value = CardState.Error("Error: ${e.message}")
                }
                .collect { cards ->
                    _cardState.value = CardState.Success(cards)
                }
        }
    }

    private val _query = mutableStateOf<String>("")
    val query: State<String> = _query
    fun updateQuery(query: String){
        if(_query.value.isNotBlank()){
            getCardByQuery()
        }
        _query.value = query
    }
    fun getCardByQuery() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _searchState.value = SearchState.Loading
            delay(500) // Debounce for 500ms
            if(_query.value.isEmpty()) {
                _searchState.value = SearchState.Idle
            } else {
                repository.getCardByQuery(_query.value)
                    .catch { e ->
                        _searchState.value = SearchState.Error("Search error: ${e.message}")
                    }
                    .collect { results ->
                        _searchState.value = if (results.isEmpty()) {
                            SearchState.Empty
                        } else {
                            SearchState.Success(results)
                        }
                    }
            }
        }
    }
}

sealed class CardState {
    object Empty : CardState()
    object Loading : CardState()
    data class Success(val cards: List<CardGalleryEntry>) : CardState()
    data class Error(val message: String) : CardState()
}

sealed class SearchState {
    object Idle : SearchState()
    object Loading : SearchState()
    object Empty : SearchState()
    data class Success(val results: List<CardGalleryEntry>) : SearchState()
    data class Error(val message: String) : SearchState()
}

class ViewModelFactory(private val repository: CardRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CardViewModel(repository) as T
    }
}