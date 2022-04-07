package fourtune.meeron.presentation.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.AgendaInfo
import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.model.TeamState
import forutune.meeron.domain.model.WorkSpaceInfo
import forutune.meeron.domain.usecase.me.GetMyWorkSpaceUserUseCase
import forutune.meeron.domain.usecase.meeting.DeleteMeetingUseCase
import forutune.meeron.domain.usecase.meeting.agenda.GetAgendaInfoUseCase
import forutune.meeron.domain.usecase.meeting.agenda.GetAgendaUseCase
import forutune.meeron.domain.usecase.meeting.team.GetTeamStatesUseCase
import forutune.meeron.domain.usecase.workspace.GetCurrentWorkspaceInfoUseCase
import forutune.meeron.domain.usecase.workspace.GetWorkspaceUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MeetingDetailViewModel @Inject constructor(
    private val getWorkspaceUser: GetWorkspaceUserUseCase,
    private val getTeamStates: GetTeamStatesUseCase,
    private val getAgenda: GetAgendaUseCase,
    private val getCurrentWorkspaceInfo: GetCurrentWorkspaceInfoUseCase,
    private val getAgendaInfo: GetAgendaInfoUseCase,
    private val getMyWorkSpaceUser: GetMyWorkSpaceUserUseCase,
    private val deleteMeeting: DeleteMeetingUseCase,
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
                    teamStates = getTeamStates(it.meeting.meetingId),
                    workspaceInfo = getCurrentWorkspaceInfo(),
                    agendaInfo = getAgendaInfo(it.meeting.meetingId),
                    isAdmin = getMyWorkSpaceUser().workspaceAdmin,
                    participantsCount = it.meeting.attends + it.meeting.absents + it.meeting.unknowns
                )
            }
        }
    }

    fun updateAgendaInfo() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(teamStates = getTeamStates(it.meeting.meetingId))
            }
        }
    }

    fun deleteMeeting(onDelete:()->Unit) {
        viewModelScope.launch {
            runCatching {
                deleteMeeting.invoke(uiState.value.meeting.meetingId)
            }.onSuccess { 
                onDelete()
            }.onFailure { 
                Timber.tag("🔥zero:deleteMeeting").e("$it")
            }
        }
    }

    data class UiState(
        val meeting: Meeting,
        val ownerNames: String = "",
        val teamStates: List<TeamState> = emptyList(),
        val workspaceInfo: WorkSpaceInfo = WorkSpaceInfo(),
        val agendaInfo: AgendaInfo = AgendaInfo(),
        val isAdmin: Boolean = false,
        val participantsCount: Int = 0
    )
}