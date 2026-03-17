package dk.mustache.spacetraders.waypoint

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dk.mustache.spacetraders.common.Trait
import dk.mustache.spacetraders.mocking.WaypointMocker
import dk.mustache.spacetraders.ui.LabelValue
import dk.mustache.spacetraders.ui.theme.Typography

object WaypointSection {
    @Composable
    fun Create(waypoint: Waypoint) {
        Column() {
            LabelValue("Symbol", waypoint.symbol)
            LabelValue("Type", waypoint.type)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                waypoint.traits.forEach {
                    TraitCard(it)
                }
            }
        }
    }

    @Composable
    fun TraitCard(trait: Trait) {
        val open = remember { mutableStateOf(false) }
        Column(
            Modifier
                .padding(top = 8.dp)
                .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                .clickable(onClick = { open.value = !open.value })
                .padding(8.dp)
        ) {
            Text(
                style = Typography.titleSmall,
                text = trait.name
            )
            if (open.value) {
                Text(trait.description)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    WaypointSection.Create(
        WaypointMocker.one()
    )
}