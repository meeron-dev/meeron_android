package fourtune.meeron.presentation.ui.team

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.Team
import forutune.meeron.domain.model.WorkspaceUser
import forutune.meeron.domain.repository.TeamRepository
import forutune.meeron.domain.usecase.me.GetMyWorkSpaceUserUseCase
import forutune.meeron.domain.usecase.team.GetTeamMemberUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AdministerTeamViewModel @Inject constructor(
    private val getTeamMembers: GetTeamMemberUseCase,
    private val teamRepository: TeamRepository,
    private val getMyWorkSpaceUser: GetMyWorkSpaceUserUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                val selectedTeam = requireNotNull(savedStateHandle.get<Team>(Const.Team))
                it.copy(
                    selectedTeam = selectedTeam,
                    teamMembers = getTeamMembers(selectedTeam.id)
                )
            }
        }

    }

    fun deletedTeamMember(user: WorkspaceUser) {
        viewModelScope.launch {
            val myWorkspaceUser = getMyWorkSpaceUser()
            val isSuccess = teamRepository.kickTeamMember(user.workspaceUserId, myWorkspaceUser.workspaceUserId)
            Timber.tag("🔥zero:deletedTeamMember").d("$isSuccess")
            _uiState.update { it.copy(teamMembers = it.teamMembers - user) }
        }
    }

    fun deleteTeam() {
        viewModelScope.launch {
            val myWorkspaceUser = getMyWorkSpaceUser()
            teamRepository.deleteTeam(uiState.value.selectedTeam.id, myWorkspaceUser.workspaceUserId)
        }
    }

    data class UiState(
        val selectedTeam: Team = Team(),
        val teamMembers: List<WorkspaceUser> = emptyList()
    )
}