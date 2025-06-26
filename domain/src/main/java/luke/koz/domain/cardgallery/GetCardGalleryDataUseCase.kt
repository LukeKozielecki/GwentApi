package luke.koz.domain.cardgallery

import kotlinx.coroutines.flow.Flow
import luke.koz.domain.model.CardGalleryEntry

interface GetCardGalleryDataUseCase {
    operator fun invoke(): Flow<List<CardGalleryEntry>>
}
