package fourtune.meeron.presentation.ui.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.model.Team
import forutune.meeron.domain.model.WorkspaceUser
import forutune.meeron.domain.usecase.me.GetMyWorkSpaceUserUseCase
import forutune.meeron.domain.usecase.team.GetTeamMemberUseCase
import forutune.meeron.domain.usecase.team.GetWorkSpaceTeamUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(
    private val getMyWorkSpaceUser: GetMyWorkSpaceUserUseCase,
    private val getWorkSpaceTeamUseCase: GetWorkSpaceTeamUseCase,
    private val getTeamMember: GetTeamMemberUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val teams = getWorkSpaceTeamUseCase()
            _uiState.update {
                it.copy(
                    teams = teams,
                    teamMembers = if (teams.isEmpty()) emptyList() else getTeamMember(teams.first().id),
                    selectedTeam = teams.firstOrNull(),
                    isAdmin = getMyWorkSpaceUser().workspaceAdmin
                )
            }
        }
    }

    fun changeTeam(team: Team) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedTeam = team,
                    teamMembers = getTeamMember(team.id)
                )
            }
        }
    }

    data class UiState(
        val teams: List<Team> = emptyList(),
        val teamMembers: List<WorkspaceUser> = emptyList(),
        val selectedTeam: Team? = null,
        val isAdmin: Boolean = false
    )
}