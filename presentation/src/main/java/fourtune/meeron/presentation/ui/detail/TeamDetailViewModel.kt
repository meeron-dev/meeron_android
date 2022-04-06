package fourtune.meeron.presentation.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.model.MeetingState
import forutune.meeron.domain.model.Team
import forutune.meeron.domain.model.WorkspaceUser
import forutune.meeron.domain.usecase.meeting.team.GetTeamMemberInMeetingUseCase
import forutune.meeron.domain.usecase.team.GetTeamUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamDetailViewModel @Inject constructor(
    private val getTeamMemberInMeeting: GetTeamMemberInMeetingUseCase,
    private val getTeamUseCase: GetTeamUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val teamId: Long = requireNotNull(savedStateHandle.get<Long>(Const.TeamId))
    private val _uiState = MutableStateFlow(
        UiState(
            meeting = requireNotNull(savedStateHandle.get(Const.Meeting)),
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    team = getTeamUseCase(teamId),
                    members = getTeamMemberInMeeting.invoke(
                        meetingId = uiState.value.meeting.meetingId,
                        teamId = teamId
                    )
                )
            }

        }
    }

    data class UiState(
        val meeting: Meeting = Meeting(),
        val team: Team = Team(),
        val members: Map<MeetingState, List<WorkspaceUser>> = emptyMap()
    )
}