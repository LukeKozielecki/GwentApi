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

    private val _showFilters = MutableStateFlow(false)
    val showFilters = _showFilters.asStateFlow()

    // Combined results state
    private val _combinedResults = MutableStateFlow<List<CardGalleryEntry>>(emptyList())
    val combinedResults = _combinedResults.asStateFlow()

    private var lastFetchedSearchResult: CardSearchResult? = null

    private var searchJob: Job? = null

    fun updateQuery(newQuery: String){
        _query.value = newQuery
        if(newQuery.isNotBlank()){
            getCardByQuery()
        } else {
            _searchState.value = SearchState.Idle
            lastFetchedSearchResult = null
        }
    }

    fun toggleExactMatches(show: Boolean) {
        _showExactMatches.value = show
        handleSearchState(lastFetchedSearchResult)
    }

    fun toggleApproximateMatches(show: Boolean) {
        _showApproximateMatches.value = show
        handleSearchState(lastFetchedSearchResult)
    }

    fun toggleFilters(show: Boolean) {
        _showFilters.value = !show
        handleSearchState(lastFetchedSearchResult)
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
                    lastFetchedSearchResult = searchResult
                    if (_query.value.isEmpty()) {
                        _searchState.value = SearchState.Idle
                    } else {
                        handleSearchState(lastFetchedSearchResult)
                    }
                }
        }
    }

    /**
     * Processes the raw search results and updates the UI [SearchState] accordingly.
     *
     * When there are no search results or the search [_query] is empty, it sets the search state to [SearchState.Idle].
     * Than it checks user preferences for displaying exact and approximate matches
     * ([_showExactMatches] and [_showApproximateMatches]). Based on these preferences and the presence of matches
     * in the [searchResult], it updates [_searchState] to either [SearchState.Empty] or [SearchState.Success].
     *
     * Finally, it triggers an update to the combined results display.
     *
     * @param searchResult The raw search results fetched from the use case.
     */
    private fun handleSearchState(searchResult: CardSearchResult?) {
        if (searchResult == null || _query.value.isEmpty()) {
            _searchState.value = SearchState.Idle
            return
        }

        val showExactIsEnabled = _showExactMatches.value
        val showApproximateIsEnabled = _showApproximateMatches.value

        val shouldShowNone = !showExactIsEnabled && !showApproximateIsEnabled
        val shouldShowExactMatches = showExactIsEnabled && searchResult.exactMatches.isNotEmpty()
        val shouldShowApproximateMatches = showApproximateIsEnabled && searchResult.approximateMatches.isNotEmpty()

        _searchState.value = when {
            shouldShowNone -> SearchState.Empty
            shouldShowExactMatches || shouldShowApproximateMatches -> SearchState.Success(searchResult)
            else -> SearchState.Empty
        }

        updateCombinedResults(lastFetchedSearchResult)
    }

    /**
     * Combines and updates the displayed search results, [_combinedResults], based on user preferences.
     *
     * If there's no [searchResult] or the [_query] is empty, the [_combinedResults] are cleared.
     * Than it checks user preferences for displaying exact and approximate matches
     * ([_showExactMatches] and [_showApproximateMatches]), and proceeds to populate local
     * variable with the result for selection.
     *
     * Finally it assigns the result to [_combinedResults]
     *
     * @param searchResult The raw search results fetched from the use case.
     */
    private fun updateCombinedResults(searchResult: CardSearchResult?) {
        if (searchResult == null || _query.value.isEmpty()) {
            _combinedResults.value = emptyList()
            return
        }

        val combined = mutableListOf<CardGalleryEntry>()
        val showExact = _showExactMatches.value
        val showApproximate = _showApproximateMatches.value

        if (showExact && searchResult.exactMatches.isNotEmpty()) {
            combined.addAll(searchResult.exactMatches)
        }

        if (showApproximate && searchResult.approximateMatches.isNotEmpty()) {
            combined.addAll(searchResult.approximateMatches)
        }

        _combinedResults.value = combined
    }
}