package dk.mustache.spacetraders

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import dagger.hilt.android.AndroidEntryPoint
import dk.mustache.spacetraders.navigation.OverviewScreenRoute
import dk.mustache.spacetraders.navigation.Route
import dk.mustache.spacetraders.navigation.SpaceTradersApp
import dk.mustache.spacetraders.ui.theme.SpaceTradersTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpaceTradersTheme {
                SpaceTradersApp()
            }
        }
    }
}