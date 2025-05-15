package luke.koz.gwentapi.ui.screen.carddetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import luke.koz.gwentapi.domain.model.CardGalleryEntry
import luke.koz.gwentapi.ui.screen.components.CardImageWithBorder


@Composable
fun CardItemDetails(card: CardGalleryEntry?, onCardClick : (Int) -> Unit) {//todo this onclick should navigate to CardArtShowcaseScreen
    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //todo SRP cut
        if (card != null) {
            CardImageWithBorder(card.artId, card.color)
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = card.name, style = MaterialTheme.typography.titleMedium)
                Text(text = card.flavor, style = MaterialTheme.typography.bodySmall)
                Text(text = card.faction, style = MaterialTheme.typography.bodySmall)
                Text(text = card.rarity, style = MaterialTheme.typography.bodySmall)
            }
        } else {
            Column {
                CardImageWithBorder(-1, "gold")
                Text("Ops. Something went wrong")
            }
        }
    }
}

