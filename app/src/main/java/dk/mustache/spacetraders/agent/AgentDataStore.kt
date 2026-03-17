package dk.mustache.spacetraders.agent

import dk.mustache.spacetraders.api.DataResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AgentDataStore @Inject constructor(
    private val agentRepository: AgentRepository,
) {
    private val _myAgent = MutableStateFlow(Agent.EMPTY)
    val myAgent = _myAgent.asStateFlow()

    suspend fun fetchMyAgent(): DataResult<Agent> {
        val result = agentRepository.getMyAgent()
        if (result is DataResult.Success) {
            _myAgent.value = result.data
        }
        return result
    }
}