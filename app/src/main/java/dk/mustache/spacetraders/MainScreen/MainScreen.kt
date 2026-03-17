package dk.mustache.spacetraders.MainScreen

import android.widget.ScrollView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dk.mustache.spacetraders.MainScreen.MainScreen.CreateInstance
import dk.mustache.spacetraders.agent.Agent
import dk.mustache.spacetraders.common.WaypointId
import dk.mustache.spacetraders.contracts.Contract
import dk.mustache.spacetraders.contracts.ContractEvent
import dk.mustache.spacetraders.contracts.ContractsSection
import dk.mustache.spacetraders.mocking.ContractMocker
import dk.mustache.spacetraders.mocking.WaypointMocker
import dk.mustache.spacetraders.ui.LabelValue
import dk.mustache.spacetraders.ui.theme.MyTextStyle
import dk.mustache.spacetraders.ui.theme.Typography
import dk.mustache.spacetraders.waypoint.Waypoint
import dk.mustache.spacetraders.waypoint.WaypointSection

object MainScreen {
    @Composable
    fun Create(innerPadding: PaddingValues) {
        val viewModel: MainViewModel = hiltViewModel()
        val agent by viewModel.agent.collectAsState()
        val contracts by viewModel.contracts.collectAsState()
        val waypoint by viewModel.currentWayPoint.collectAsState()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            CreateInstance(agent, contracts, waypoint, viewModel::onContractEvent)
            if (viewModel.loading.collectAsState().value) {
                Text(text = "Loading...")
            }
        }
    }

    @Composable
    fun CreateInstance(
        agent: Agent,
        contracts: List<Contract>,
        waypoint: Waypoint,
        onContractEvent: (ContractEvent) -> Unit
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
        contracts = ContractMocker.standardSet(),
        WaypointMocker.one(),
        onContractEvent = {}
    )
}