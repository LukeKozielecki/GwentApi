package luke.koz.carddetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import luke.koz.domain.model.CardDetailsEntry
import luke.koz.carddetails.components.CardImageOverlay

//todo onCardClick should navigate to ~FullscreenCardWithAuthorShowcase
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CardItemDetails(
    card: CardDetailsEntry?,
    onCardClick: (Int) -> Unit,
    imageLoader: ImageLoader
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (card != null) {
            // Clickable card image
            Box(Modifier.clickable { onCardClick(card.cardId) }) {
                luke.koz.presentation.CardImageWithBorder(
                    card.artId,
                    card.color,
                    imageLoader
                )
                CardImageOverlay(card, imageLoader)
            }

            // Main content column
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                // Title section
                Text(
                    text = card.name,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Flavor text
                Text(
                    text = card.flavor,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Stats grid
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem("Power", card.power)
                    /*StatItem("Reach", card.reach.toString()) // this is apparently not used anymore*/
                    StatItem("Armor", card.armor)
                    StatItem("Provisions", card.provision)
                }

                // Details section
                DetailSection(
                    items = listOf(
                        "Faction" to card.faction,
                        "Rarity" to card.rarity,
                        "Type" to card.type,
                        "Color" to card.color
                    )
                )

                // Ability text
                Text(
                    text = card.abilityHtml,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Categories chips
                val categories: List<String>? = card.category.takeIf { it.isNotEmpty() }?.split(", ")?.map { it.trim() }
                if (!categories.isNullOrEmpty()) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        categories.forEach { category ->
                            FilterChip(
                                selected = false,
                                onClick = {},
                                label = { Text(category) }
                            )
                        }
                    }
                }
                val ledger: List<String>? = card.keywordHtml
                    ?.takeIf { it.isNotEmpty() }
                    ?.split("\n")
                    ?.mapNotNull { entry ->
                        val pattern = """<span class="keyword">(.*?)</span>(.*)""".toRegex()
                        val matchResult = pattern.find(entry.trim())

                        matchResult?.let {
                            val (keyword, description) = it.destructured
                            "$keyword ${description.trim().replace(Regex("\\s+"), " ")}"
                        }
                    }
                    ?.filter { it.isNotEmpty() }
                    ?.takeIf { it.isNotEmpty() }
                if (!ledger.isNullOrEmpty()) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("Keywords:", style = MaterialTheme.typography.labelLarge)
                        Spacer(modifier = Modifier.height(4.dp))
                        ledger.forEach { entry ->
                            Text(
                                text = entry,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        } else {
            // Error state
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                luke.koz.presentation.CardImageWithBorder(-1, "gold", imageLoader = imageLoader)
                Text(
                    text = "Card information not available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: Int) {
    if (value > 0) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}


@Composable
private fun DetailSection(items: List<Pair<String, String>>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        items.forEach { (label, value) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
