package luke.koz.search.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import luke.koz.domain.search.SearchCardsUseCase
import luke.koz.presentation.state.SearchState

class SearchViewModel (
    private val searchCardsUseCase: SearchCardsUseCase
) : ViewModel() {

    private val _searchState = mutableStateOf<SearchState>(SearchState.Idle)
    val searchState: State<SearchState> = _searchState

    private val _query = mutableStateOf<String>("")
    val query: State<String> = _query

    fun updateQuery(newQuery: String){
        _query.value = newQuery
        if(newQuery.isNotBlank()){
            getCardByQuery()
        } else {
            _searchState.value = SearchState.Idle
        }
    }

    fun getCardByQuery() {
        viewModelScope.launch {
            _searchState.value = SearchState.Loading

            searchCardsUseCase(_query.value)
                .catch { e ->
                    _searchState.value = SearchState.Error("Search error: ${e.message}")
                }
                .collectLatest { results ->
                    _searchState.value = when {
                        _query.value.isEmpty() -> SearchState.Idle
                        results.isEmpty() -> SearchState.Empty
                        else -> SearchState.Success(results)
                    }
                }
        }
    }
}