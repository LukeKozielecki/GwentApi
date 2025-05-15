package luke.koz.gwentapi.ui.screen.components.cardlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import luke.koz.gwentapi.domain.model.CardGalleryEntry
import luke.koz.gwentapi.ui.screen.components.CardImageWithBorder


@Composable
internal fun CardItem(card: CardGalleryEntry, onCardClick : (Int) -> Unit) {
    Row (
        modifier = Modifier
            .clickable { onCardClick(card.id) }
    ) {
        CardImageWithBorder(card.artId, card.color)
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = card.name, style = MaterialTheme.typography.titleMedium)
            Text(text = card.flavor, style = MaterialTheme.typography.bodySmall)
            Text(text = card.artId.toString(), style = MaterialTheme.typography.bodySmall)
        }
    }
}