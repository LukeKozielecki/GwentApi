package luke.koz.domain.cardgallery

import kotlinx.coroutines.flow.Flow
import luke.koz.domain.model.CardGalleryData

interface RefreshCardGalleryDataUseCase {
    suspend fun invoke(forceRefreshCards: Boolean = false): CardGalleryData
    fun observeAuthAndInternetChanges(): Flow<Pair<String?, Boolean>>
}
