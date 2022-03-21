package fourtune.meeron.presentation.ui.createmeeting.participants

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.model.Team
import forutune.meeron.domain.model.WorkspaceUser
import forutune.meeron.domain.usecase.GetWorkSpaceTeamUseCase
import forutune.meeron.domain.usecase.user.GetWorkspaceUserUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateMeetingParticipantsViewModel @Inject constructor(
    private val getWorkSpaceTeamUseCase: GetWorkSpaceTeamUseCase,
    private val getWorkspaceUserUseCase: GetWorkspaceUserUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            _uiState.update {
                val teams = getWorkSpaceTeamUseCase()
                val meeting: Meeting = requireNotNull(savedStateHandle[Const.Meeting])
                it.copy(
                    meeting = meeting,
                    teams = teams,
                    teamMembers = getWorkspaceUserUseCase(teamId = teams.first().id),
                    fixedOwner = getWorkspaceUserUseCase(workspaceUserIds = meeting.ownerIds.toLongArray())
                )
            }
        }
    }

    fun getTeamMembers(id: Long) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    teamMembers = getWorkspaceUserUseCase(teamId = id)
                )
            }
        }
    }

    fun onSearch(text: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            _uiState.update {
                it.copy(searchedUsers = getWorkspaceUserUseCase.invoke(text = text))
            }
        }
    }

    data class UiState(
        val meeting: Meeting = Meeting(),
        val teams: List<Team> = emptyList(),
        val teamMembers: List<WorkspaceUser> = emptyList(),
        val fixedOwner: List<WorkspaceUser> = emptyList(),
        val searchedUsers: List<WorkspaceUser> = emptyList()
    )

    sealed interface Event {
        object Action : Event
        object Previous : Event
        class Next(val participants: List<WorkspaceUser>) : Event
        class SelectTeam(val id: Long) : Event
    }
}