package luke.koz.search.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * Material 3 custom implementation of [SearchBar] for purpose of browsing Card Gallery data.
 *
 * It uses the styling and basic input features of the Material 3 SearchBar
 * and its [SearchBarDefaults.InputField], but intentionally keeps the search bar in a
 * collapsed (non-expanded) state. Search results and additional filtering functionality
 * are displayed and managed externally, rather than within the SearchBar's own content block.
 *
 * @param query The current text query displayed in the search bar.
 * @param onQueryChange Callback invoked when the user types or modifies the search query.
 * @param onSearch Callback invoked when the user explicitly triggers a search action
 * followed by automatic hiding of keyboard and focus clearance.
 * @param onClearQuery Callback invoked when the user clicks the "clear query" icon,
 * requesting the input field to be emptied.
 * @param modifier Optional modifier to be applied to the root [Box] of this composable.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchGalleryBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClearQuery: () -> Unit,
    modifier: Modifier = Modifier
) {
    val expanded = false
    val onExpandedChange : (Boolean) -> Unit = { /* As per current implementation it is meant to stay collapsed */ }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Box(modifier = modifier.fillMaxWidth()) {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = onQueryChange,
                    onSearch = {
                        onSearch()
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    },
                    expanded = expanded,
                    onExpandedChange = onExpandedChange,
                    enabled = true,
                    placeholder = { Text(
                        text = "Search the gallery",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis)
                    },
                    trailingIcon = {
                        if (query.isNotBlank()) {
                            IconButton(
                                onClick = onClearQuery,
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear search query"
                                )
                            }
                        }
                    },
                    interactionSource = null,
                )
            },
            expanded = expanded,
            onExpandedChange = onExpandedChange,
            modifier = Modifier.align(Alignment.TopCenter).offset(y = (-4).dp),// the offset was moved here
            //i don't know why this is necessary. otherwise, there is ~4.dp padding towards top
            shape = SearchBarDefaults.inputFieldShape,
            tonalElevation = SearchBarDefaults.TonalElevation,
            shadowElevation = SearchBarDefaults.ShadowElevation,
            windowInsets = WindowInsets(top = 0.dp, bottom = 0.dp),
        ) { /* Content block
             * As per current implementation this searchbar performs filtering functionality via
             * provided properties.
             * This content block is typically displayed, as per documentation, when user interacts with
             * search bar. It is redundant in current implementation
             */
        }
    }
}