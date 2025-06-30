package luke.koz.domain.search

import kotlinx.coroutines.flow.Flow
import luke.koz.domain.model.CardSearchResult

interface SearchCardsUseCase {
    operator fun invoke(query: String): Flow<CardSearchResult>
}