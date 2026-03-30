package dk.mustache.spacetraders.screens.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dk.mustache.spacetraders.common.architecture.ScreenEvent
import dk.mustache.spacetraders.common.dataclasses.WaypointId
import dk.mustache.spacetraders.features.agent.Agent
import dk.mustache.spacetraders.features.contracts.ContractModel
import dk.mustache.spacetraders.features.contracts.ContractsSection
import dk.mustache.spacetraders.features.waypoint.Waypoint
import dk.mustache.spacetraders.features.waypoint.WaypointSection
import dk.mustache.spacetraders.mocking.ContractMocker
import dk.mustache.spacetraders.mocking.WaypointMocker
import dk.mustache.spacetraders.screens.overview.OverviewScreen.CreateInstance
import dk.mustache.spacetraders.ui.LabelValue
import dk.mustache.spacetraders.ui.theme.MyTextStyle

object OverviewScreen {
    @Composable
    fun Create(innerPadding: PaddingValues) {
        val viewModel: OverviewViewModel = hiltViewModel()
        val agent by viewModel.myAgent.collectAsState()
        val contracts by viewModel.myContracts.collectAsState()
        val waypoint by viewModel.currentWayPoint.collectAsState()

        val exception by viewModel.exception.collectAsState(null)
        val loading by viewModel.loadingData.collectAsState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            CreateInstance(agent, contracts, waypoint, viewModel::onEvent)
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
    fun CreateInstance(
        agent: Agent,
        contracts: List<ContractModel>,
        waypoint: Waypoint,
        onContractEvent: (ScreenEvent) -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(8.dp)
        ) {
            AgentSection(agent)
            Text(
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                style = MyTextStyle.SectionTitle,
                text = "HQ:"
            )
            WaypointSection.Create(waypoint)
            Text(
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                style = MyTextStyle.SectionTitle,
                text = "Contracts:"
            )
            ContractsSection.Create(contracts, onContractEvent)
        }
    }

    @Composable
    private fun AgentSection(agent: Agent) {
        Column {
            LabelValue("Agent", agent.symbol)
            LabelValue("Credits", agent.credits.toString())
            LabelValue("Ships", agent.shipCount.toString())
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    CreateInstance(
        Agent(
            accountId = "123",
            symbol = "TestUser",
            headquarters = WaypointId("X1", "HF93", "A1"),
            credits = 1000000,
            startingFaction = "GALACTIC",
            shipCount = 1
        ),
        contracts = ContractMocker.standardSetModel(),
        WaypointMocker.one(),
        onContractEvent = {}
    )
}