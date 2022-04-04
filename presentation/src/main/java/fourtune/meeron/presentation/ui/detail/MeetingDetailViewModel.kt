package fourtune.meeron.presentation.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.model.TeamState
import forutune.meeron.domain.usecase.meeting.GetTeamStatesUseCase
import forutune.meeron.domain.usecase.meeting.agenda.GetAgendasUseCase
import forutune.meeron.domain.usecase.workspace.GetWorkspaceUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeetingDetailViewModel @Inject constructor(
    private val getWorkspaceUser: GetWorkspaceUserUseCase,
    private val getTeamStates: GetTeamStatesUseCase,
    private val getAgendasUseCase: GetAgendasUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState(requireNotNull(savedStateHandle.get<Meeting>(Const.Meeting))))
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                val ownersName =
                    getWorkspaceUser(workspaceUserIds = uiState.value.meeting.ownerIds.toLongArray()).map { it.nickname }
                it.copy(
                    ownerNames = ownersName.joinToString(),
                    teamStates = getTeamStates(it.meeting.meetingId)
                )
            }
        }
    }

    data class UiState(
        val meeting: Meeting,
        val ownerNames: String = "",
        val teamStates: List<TeamState> = emptyList()
    )
}