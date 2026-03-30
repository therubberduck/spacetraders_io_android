package dk.mustache.spacetraders.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dk.mustache.spacetraders.screens.biz.BizScreen
import dk.mustache.spacetraders.screens.overview.OverviewScreen

@PreviewScreenSizes
@Composable
fun SpaceTradersApp() {
    var currentDestination: Route by rememberSerializable { mutableStateOf(OverviewScreenRoute) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = it.route == currentDestination,
                    onClick = { currentDestination = it.route }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            when(currentDestination) {
                OverviewScreenRoute -> OverviewScreen.Create(innerPadding)
                BizScreenRoute -> BizScreen.Create(innerPadding)
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
    val route: Route
) {
    OVERVIEW("OverView", Icons.Default.Home, OverviewScreenRoute),
    BIZ("Biz", Icons.Default.Favorite, BizScreenRoute),
    MAP("Map", Icons.Default.AccountBox, OverviewScreenRoute),
}