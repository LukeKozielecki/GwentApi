package luke.koz.gwentapi.ui.screen.components.cardlist

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import luke.koz.gwentapi.domain.model.CardGalleryEntry
import luke.koz.gwentapi.ui.screen.components.cardstate.EmptyState


@Composable
fun CardList(cards: List<CardGalleryEntry>, onCardClick : (Int) -> Unit) {
    if (cards.isEmpty()) {
        EmptyState()
    } else {
        LazyColumn {
            items(cards) { card ->
                CardItem(card = card, onCardClick)
                HorizontalDivider()
            }
        }
    }
}