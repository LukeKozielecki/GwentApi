package luke.koz.search.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import luke.koz.domain.model.CardGalleryRepository
import luke.koz.presentation.state.SearchState

class SearchViewModel (private val repository: CardGalleryRepository) : ViewModel() {

    private val _searchState = mutableStateOf<SearchState>(SearchState.Idle)
    val searchState: State<SearchState> = _searchState

    private var searchJob: Job? = null

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
            delay(500)
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