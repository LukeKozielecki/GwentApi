package luke.koz.domain.search

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import luke.koz.domain.model.CardGalleryEntry
import luke.koz.domain.repository.CardGalleryRepository

class SearchCardsUseCaseImpl(
    private val cardGalleryRepository: CardGalleryRepository
) : SearchCardsUseCase {

    private val _debounceTimeMillis : Long = 500L

    @OptIn(ExperimentalCoroutinesApi::class)
    override operator fun invoke(query: String): Flow<List<CardGalleryEntry>> {
        return flowOf(query)
            .distinctUntilChanged()
            .flatMapLatest { searchQuery ->
                if (searchQuery.isBlank()) {
                    delay(_debounceTimeMillis) //this is here to provide consistent UX when transitioning from non-empty query to empty query
                    flowOf(emptyList())
                } else {
                    delay(_debounceTimeMillis)
                    cardGalleryRepository.getCardByQuery(searchQuery)
                }
            }
    }
}