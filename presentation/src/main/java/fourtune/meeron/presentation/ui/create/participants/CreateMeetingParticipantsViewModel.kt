package fourtune.meeron.presentation.ui.create.participants

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.model.Team
import forutune.meeron.domain.model.WorkspaceUser
import forutune.meeron.domain.usecase.GetUserUseCase
import forutune.meeron.domain.usecase.GetWorkSpaceTeamUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateMeetingParticipantsViewModel @Inject constructor(
    private val getWorkSpaceTeamUseCase: GetWorkSpaceTeamUseCase,
    private val getUserUseCase: GetUserUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())

    val uiState = _uiState.asStateFlow()
    init {
        viewModelScope.launch {
            _uiState.update {
                val teams = getWorkSpaceTeamUseCase()
                it.copy(
                    meeting = requireNotNull(savedStateHandle[Const.Meeting]),
                    teams = teams,
                    teamMembers = getUserUseCase(teams.first().id)
                )
            }
        }
    }

    fun getTeamMembers(id: Long) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    teamMembers = getUserUseCase(teamId = id)
                )
            }
        }
    }


    data class UiState(
        val meeting: Meeting = Meeting(),
        val teams: List<Team> = emptyList(),
        val teamMembers: List<WorkspaceUser> = emptyList()
    )

    sealed interface Event {
        object Action : Event
        object Previous : Event
        class Next(val participants: List<WorkspaceUser>) : Event
        class SelectTeam(val id: Long) : Event
    }
}