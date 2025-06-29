package luke.koz.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import luke.koz.domain.model.CardGalleryEntry
import luke.koz.domain.model.CardSearchResult
import luke.koz.domain.search.SearchCardsUseCase
import luke.koz.presentation.state.SearchState

class SearchViewModel (
    private val searchCardsUseCase: SearchCardsUseCase
) : ViewModel() {

    private val _searchState = MutableStateFlow<SearchState>(SearchState.Idle)
    val searchState: StateFlow<SearchState> = _searchState

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    // State for match type toggles
    private val _showExactMatches = MutableStateFlow(true)
    val showExactMatches = _showExactMatches.asStateFlow()

    private val _showApproximateMatches = MutableStateFlow(true)
    val showApproximateMatches = _showApproximateMatches.asStateFlow()

    // Combined results state
    private val _combinedResults = MutableStateFlow<List<CardGalleryEntry>>(emptyList())
    val combinedResults = _combinedResults.asStateFlow()

    private var searchJob: Job? = null

    fun updateQuery(newQuery: String){
        _query.value = newQuery
        if(newQuery.isNotBlank()){
            getCardByQuery()
        } else {
            _searchState.value = SearchState.Idle
        }
    }

    fun toggleExactMatches(show: Boolean) {
        _showExactMatches.value = show
        updateCombinedResults()
    }

    fun toggleApproximateMatches(show: Boolean) {
        _showApproximateMatches.value = show
        updateCombinedResults()
    }

    private fun getCardByQuery() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _searchState.value = SearchState.Loading

            searchCardsUseCase(_query.value)
                .catch { e ->
                    _searchState.value = SearchState.Error("Search error: ${e.message}")
                }
                .collectLatest { searchResult ->
                    if (_query.value.isEmpty()) {
                        _searchState.value = SearchState.Idle
                    } else {
                        handleSearchResult(searchResult)
                    }
                }
        }
    }

    private fun handleSearchResult(searchResult: CardSearchResult) {
        _searchState.value = when {
            searchResult.exactMatches.isEmpty() && searchResult.approximateMatches.isEmpty() ->
                SearchState.Empty
            else ->
                SearchState.Success(searchResult)
        }

        updateCombinedResults()
    }

    private fun updateCombinedResults() {
        val currentState = _searchState.value
        if (currentState !is SearchState.Success) {
            _combinedResults.value = emptyList()
            return
        }

        val searchResult = currentState.searchResults
        val combined = mutableListOf<CardGalleryEntry>()

        if (_showExactMatches.value && searchResult.exactMatches.isNotEmpty()) {
            combined.addAll(searchResult.exactMatches)
        }

        if (_showApproximateMatches.value && searchResult.approximateMatches.isNotEmpty()) {
            combined.addAll(searchResult.approximateMatches)
        }

        _combinedResults.value = combined
    }
}