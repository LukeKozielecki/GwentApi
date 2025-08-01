package luke.koz.search.components

import androidx.compose.runtime.Composable
import coil3.ImageLoader
import luke.koz.domain.model.CardGalleryEntry
import luke.koz.presentation.card.CardList
import luke.koz.presentation.statusscreen.SuccessStatusScreen

@Composable
internal fun FilteredCardList(
    cards: List<CardGalleryEntry>,
    onCardClick: (Int) -> Unit,
    imageLoader: ImageLoader
) {
    //Todo(low-priority): this could have like-submit database functionality
    SuccessStatusScreen(
        content = {
            CardList(
                cards = cards,
                onCardClick = onCardClick,
                onToggleLike = { _, _ ->

                },
                imageLoader = imageLoader,
                shouldShowLikeButton = false
            )
        }
    )
}