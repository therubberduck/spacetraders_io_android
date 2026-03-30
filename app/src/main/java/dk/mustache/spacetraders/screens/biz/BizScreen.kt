package dk.mustache.spacetraders.screens.biz

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dk.mustache.spacetraders.common.architecture.ScreenEvent
import dk.mustache.spacetraders.features.fleet.Ship
import dk.mustache.spacetraders.features.fleet.ShipComponent
import dk.mustache.spacetraders.features.fleet.ShipModuleStats
import dk.mustache.spacetraders.features.fleet.ShipMountStats
import dk.mustache.spacetraders.mocking.ShipMocker
import dk.mustache.spacetraders.ui.LabelValue
import dk.mustache.spacetraders.ui.theme.Typography

object BizScreen {

    @Composable
    fun Create(innerPadding: PaddingValues) {

        val viewModel: BizViewModel = hiltViewModel()
        val myFleet by viewModel.myShips.collectAsState()

        val loading by viewModel.loadingData.collectAsState()
        val exception by viewModel.exception.collectAsState()


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            CreateInstance(myFleet)
            if (loading) {
                Box(
                    modifier = Modifier
                        .border(1.dp, Color.Black, shape = RoundedCornerShape(4.dp))
                        .background(Color.White, shape = RoundedCornerShape(4.dp))
                        .padding(16.dp)
                ) {
                    Text(text = "Loading...")
                }
            }
            if (exception != null) {
                Dialog(
                    onDismissRequest = { viewModel.onEvent(ScreenEvent.ClearException) },
                    content = {
                        Text(exception?.message ?: "Unknown error")
                        Button(onClick = { viewModel.onEvent(ScreenEvent.ClearException) }) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun CreateInstance(myFleet: List<Ship>) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            myFleet.forEach { ship ->
                ShipCard(ship)
            }
        }
    }

    @Composable
    private fun ShipCard(ship: Ship) {
        var open by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable(onClick = { open = !open })
                .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
                .background(Color.White, RoundedCornerShape(4.dp))
                .padding(8.dp)
        ) {
            Text(
                style = Typography.titleSmall,
                text = ship.symbol
            )
            Row() {
                LabelValue("System", ship.systemSymbol)
                Spacer(Modifier.width(16.dp))
                LabelValue("Waypoint", ship.waypointSymbol)
            }
            Row() {
                LabelValue("Status", ship.status.label)
                Spacer(Modifier.width(16.dp))
                LabelValue("Flight Mode", ship.flightMode.label)
            }
            Row() {
                LabelValue("Cargo", ship.cargo.toString())
                Spacer(Modifier.width(16.dp))
                LabelValue("Fuel", ship.fuel.toString())
            }
            if (open) {
                ship.components.forEach { component ->
                    ComponentSection(component)
                }
            }
        }
    }

    @Composable
    private fun ComponentSection(component: ShipComponent) {
        Column {
            Text(
                style = Typography.labelMedium,
                text = component.name
            )
            Text(
                style = Typography.bodySmall,
                text = component.description
            )
            if (component.stats.notEmpty()) {
                FlowRow() {
                    if (component.stats is ShipModuleStats) {
                        if (component.stats.capacity != null)
                            LabelValue("Capacity", component.stats.capacity.toString())
                        if (component.stats.capacity != null && component.stats.range != null)
                            Spacer(Modifier.width(16.dp))
                        if (component.stats.range != null)
                            LabelValue("Range", component.stats.range.toString())
                    } else if (component.stats is ShipMountStats) {
                        if (component.stats.strength != null)
                            LabelValue("Strength", component.stats.strength.toString())
                        if (component.stats.strength != null && component.stats.deposits != null)
                            Spacer(Modifier.width(16.dp))
                        if (component.stats.deposits != null)
                            LabelValue("Deposits", component.stats.deposits.toString())
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun BizScreenPreview() {
    BizScreen.CreateInstance(
        ShipMocker.standardSet(2)
    )
}

