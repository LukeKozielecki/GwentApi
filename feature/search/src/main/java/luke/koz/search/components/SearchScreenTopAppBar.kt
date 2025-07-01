package luke.koz.search.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import luke.koz.presentation.scaffold.CoreTopAppBar
import luke.koz.presentation.scaffold.components.CoreTopAppBarTitle
import luke.koz.search.SearchScreenLayout
import luke.koz.search.model.SearchScreenContentActions
import luke.koz.search.model.SearchScreenContentUiState

/**
 * A [CoreTopAppBar] composable implementation for [SearchScreenLayout] that dynamically switches
 * between a static title and an interactive search bar.
 *
 * Handles the visual transition between the title ([CoreTopAppBarTitle]),
 * and the search bar ([SearchGalleryBar]). Manages focus for the search input,
 * clears focus on confirmation and hides searchbar.
 *
 * @param uiState The current UI state ([SearchScreenContentUiState]) which provides
 * information necessary for composition.
 * @param actions The selection of actions ([SearchScreenContentActions]) that can be performed
 * by user interactions within this composable.
 * @param focusManager The [FocusManager] instance used to control and clear keyboard focus.
 * @param focusRequester The [FocusRequester] instance used to request focus
 * for the search input field when the search bar becomes active.
 * @param modifier Optional [Modifier], currently it is applied to the [CoreTopAppBar].
 */
@Composable
internal fun SearchScreenTopAppBar(
    uiState: SearchScreenContentUiState,
    actions: SearchScreenContentActions,
    focusManager: FocusManager,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier) {
    CoreTopAppBar(
        title = {
            AnimatedContent(
                targetState = uiState.isSearchBarActive,
                transitionSpec = {
                    if (targetState) {
                        slideInVertically { height -> height } togetherWith slideOutVertically { height -> -height }
                    } else {
                        slideInVertically { height -> -height } togetherWith slideOutVertically { height -> height }
                    }
                }, label = "TopAppBarContentTransition",
                modifier = Modifier.height(64.dp)
            ) { active ->
                if (active) {
                    LaunchedEffect (Unit){
                        if (uiState.query.isEmpty()) {
                            focusRequester.requestFocus()
                        }
                    }
                    SearchGalleryBar(
                        query = uiState.query,
                        onQueryChange = actions.onQueryChange,
                        onSearch = {
                            actions.onToggleSearchBarActive(false)
                            focusManager.clearFocus()
                        },
                        onClearQuery = actions.onClearQuery,
                        onNavigateBack = {
                            actions.onToggleSearchBarActive(false)
                            focusManager.clearFocus()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .focusRequester(focusRequester)
                    )
                } else {
                    Box(modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                        CoreTopAppBarTitle(modifier.fillMaxHeight())
                    }
                }
            }
        },
        actions = {
            IconButton(onClick = {
                actions.onToggleSearchBarActive(!uiState.isSearchBarActive)
            }) {
                Icon(imageVector = if(!uiState.isSearchBarActive) {
                    Icons.Default.Search
                } else {
                    Icons.Default.KeyboardArrowDown
                }, contentDescription = if (!uiState.isSearchBarActive) {
                    "Enable search bar"
                } else {
                    "Hide search bar"
                })
            }

            IconButton(
                onClick = { actions.onToggleFiltersMatches(uiState.showFilters) }
            ) {
                Icon(
                    painter = painterResource(luke.koz.presentation.R.drawable.outline_filter_list_24),
                    contentDescription = if (uiState.showFilters) "Hide filters" else "Show filters"
                )
            }
        }
    )
}