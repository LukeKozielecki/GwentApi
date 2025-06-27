package luke.koz.domain.search

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import luke.koz.domain.model.CardGalleryEntry
import luke.koz.domain.repository.CardGalleryRepository

class SearchCardsUseCaseImpl(
    private val cardGalleryRepository: CardGalleryRepository
) : SearchCardsUseCase {
    @OptIn(ExperimentalCoroutinesApi::class)
    override operator fun invoke(query: String): Flow<List<CardGalleryEntry>> {
        return flowOf(query)
            .distinctUntilChanged()
            .filter { it.isNotBlank() }
            .flatMapLatest { searchQuery ->
                delay(500)
                cardGalleryRepository.getCardByQuery(searchQuery)
            }
    }
}