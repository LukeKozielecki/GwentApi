package luke.koz.presentation.scaffold

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import luke.koz.presentation.scaffold.components.CoreTopAppBarTitle
import luke.koz.presentation.theme.GwentApiTheme

/**
 * Implements a generic reusable [TopAppBar] with customizable parameters. It acts as a wrapper
 * around material3's TopAppBar providing a single source for consistent base implementation.
 *
 * @param modifier Modifier for the [TopAppBar].
 * @param title Composable for the central title area. Defaults to [CoreTopAppBarTitle].
 * @param navigationIcon Composable for the leading icon, typically a back or drawer icon.
 * @param actions Composable for trailing action. This should typically be [IconButton].
 * The default layout here is a Row, so icons inside will be placed horizontally.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GwentTopAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = { CoreTopAppBarTitle() },
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        modifier = modifier,
        title = title,
        navigationIcon = navigationIcon,
        actions = actions
    )
}

@Preview
@Composable
private fun GwentTopAppBarPrev() {
    GwentApiTheme {
        GwentTopAppBar(
            modifier = Modifier,
            title = { CoreTopAppBarTitle() },
        )
    }
}