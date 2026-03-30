package dk.mustache.spacetraders

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
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