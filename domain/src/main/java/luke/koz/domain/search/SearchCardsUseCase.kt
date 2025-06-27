package luke.koz.domain.search

import kotlinx.coroutines.flow.Flow
import luke.koz.domain.model.CardGalleryEntry

interface SearchCardsUseCase {
    operator fun invoke(query: String): Flow<List<CardGalleryEntry>>
}