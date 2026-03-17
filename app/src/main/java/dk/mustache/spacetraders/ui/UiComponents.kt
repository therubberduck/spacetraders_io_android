package dk.mustache.spacetraders.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dk.mustache.spacetraders.ui.theme.Typography

@Composable
fun LabelValue(title: String, value: String) {
    Row() {
        Text(
            style = Typography.labelSmall,
            text = "$title: "
        )
        Text(value)
    }
}