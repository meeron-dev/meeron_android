package fourtune.meeron.presentation.ui.home.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.model.Team
import forutune.meeron.domain.model.WorkspaceUser
import forutune.meeron.domain.usecase.me.GetMyWorkSpaceUserUseCase
import forutune.meeron.domain.usecase.team.GetTeamMemberUseCase
import forutune.meeron.domain.usecase.team.GetWorkSpaceTeamUseCase
import forutune.meeron.domain.usecase.workspace.GetNotJoinedTeamWorkspaceUserUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(
    private val getMyWorkSpaceUser: GetMyWorkSpaceUserUseCase,
    private val getWorkSpaceTeamUseCase: GetWorkSpaceTeamUseCase,
    private val getTeamMember: GetTeamMemberUseCase,
    private val getNotJoinedTeamWorkspaceUser: GetNotJoinedTeamWorkspaceUserUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<TeamEvent>()
    val event = _event.asSharedFlow()

    private var initJob: Job? = null

    init {
        initJob = viewModelScope.launch {
            runCatching {
                val teams = getWorkSpaceTeamUseCase()
                val teamMembers = if (teams.isEmpty()) emptyList() else getTeamMember(teams.first().id)
                val selectedTeam = teams.firstOrNull()?.let(TeamState::Normal) ?: TeamState.None
                Timber.tag("🔥zero:init").w("$selectedTeam")
                _uiState.update {
                    it.copy(
                        teams = teams,
                        teamMembers = teamMembers,
                        selectedTeam = selectedTeam,
                        isAdmin = getMyWorkSpaceUser().workspaceAdmin,
                        showNone = getNotJoinedTeamWorkspaceUser().isNotEmpty()
                    )
                }
            }.onFailure { Timber.tag("🔥zero:teamViewModel").e("$it") }
        }
    }

    fun changeTeam(team: Team) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedTeam = TeamState.Normal(team),
                    teamMembers = getTeamMember(team.id)
                )
            }
        }
    }

    fun fetch() {
        viewModelScope.launch {
            initJob?.join()
            runCatching {
                val teams = getWorkSpaceTeamUseCase()
                val selectedTeam = uiState.value.selectedTeam
                val notJoinedTeamWorkspaceUser = getNotJoinedTeamWorkspaceUser()
                _uiState.update {
                    it.copy(
                        teams = teams,
                        teamMembers = when {
                            teams.isEmpty() -> emptyList()
                            selectedTeam is TeamState.Normal -> getTeamMember(selectedTeam.team.id)
                            selectedTeam is TeamState.None -> notJoinedTeamWorkspaceUser
                            else -> emptyList()
                        },
                        showNone = notJoinedTeamWorkspaceUser.isNotEmpty(),
                    )
                }
            }.onFailure { Timber.tag("🔥zero:teamViewModel").e("$it") }
        }
    }

    fun getNotJoinedTeamMembers() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedTeam = TeamState.None,
                    teamMembers = getNotJoinedTeamWorkspaceUser()
                )
            }

        }
    }

    data class UiState(
        val teams: List<Team> = emptyList(),
        val teamMembers: List<WorkspaceUser> = emptyList(),
        val selectedTeam: TeamState = TeamState.None,
        val isAdmin: Boolean = false,
        val showNone: Boolean = false
    )

    sealed class TeamState(val team: Team) {
        class Normal(team: Team) : TeamState(team)
        object None : TeamState(Team())
    }
}