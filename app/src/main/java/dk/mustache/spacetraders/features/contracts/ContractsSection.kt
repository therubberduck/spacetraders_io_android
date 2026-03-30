package dk.mustache.spacetraders.features.contracts

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dk.mustache.spacetraders.common.architecture.ScreenEvent
import dk.mustache.spacetraders.mocking.ContractMocker
import dk.mustache.spacetraders.ui.LabelValue
import dk.mustache.spacetraders.ui.theme.Orange
import dk.mustache.spacetraders.ui.theme.Typography

data class ContractModel(
    val id: String,
    val state: Contract.ContractState,
    val title: String,
    val faction: String,
    val deadline: String,
    val expiration: String
)

object ContractsSection {
    @Composable
    fun Create(contracts: List<ContractModel>, onEvent: (ScreenEvent) -> Unit) {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            contracts.forEach {
                ContractCard(it, onEvent)
            }
        }
    }

    @Composable
    fun ContractCard(contract: ContractModel, onEvent: (ScreenEvent) -> Unit) {
        val open = remember { mutableStateOf(false) }
        Column(
            Modifier
                .padding(top = 8.dp)
                .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                .clickable(onClick = { open.value = !open.value })
                .padding(8.dp)
        ) {
            val contractState = contract.state
            val contractColor = when (contractState) {
                Contract.ContractState.PENDING -> Orange
                Contract.ContractState.ACCEPTED -> Color.Black
                Contract.ContractState.EXPIRED -> Color.Red
                Contract.ContractState.FULFILLED -> Color.Green
            }
            Text(
                style = Typography.titleSmall.copy(color = contractColor),
                text = contract.title
            )
            if (open.value) {
                LabelValue("Faction", contract.faction)
                if (contractState == Contract.ContractState.PENDING) {
                    LabelValue("Expiration", contract.expiration)
                }
                LabelValue("Deadline", contract.deadline)
                if (contractState == Contract.ContractState.PENDING) {
                    Button(onClick = {
                        onEvent(AcceptContract(contract.id))
                    }) {
                        Text("Accept Contract")
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    ContractsSection.Create(
        contracts = listOf(
            ContractMocker.oneModel()
        ),
        onEvent = {})
}