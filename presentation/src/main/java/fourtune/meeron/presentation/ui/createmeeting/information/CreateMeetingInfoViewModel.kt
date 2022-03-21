package fourtune.meeron.presentation.ui.createmeeting.information

import androidx.annotation.StringRes
import androidx.compose.runtime.toMutableStateMap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.model.Team
import forutune.meeron.domain.model.WorkspaceUser
import forutune.meeron.domain.usecase.GetWorkSpaceTeamUseCase
import forutune.meeron.domain.usecase.me.GetMyWorkSpaceUserUseCase
import forutune.meeron.domain.usecase.user.GetWorkspaceUserUseCase
import fourtune.meeron.presentation.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateMeetingInfoViewModel @Inject constructor(
    private val getWorkspaceUserUseCase: GetWorkspaceUserUseCase,
    getWorkSpaceTeamUseCase: GetWorkSpaceTeamUseCase,
    getMyWorkSpaceUserUseCase: GetMyWorkSpaceUserUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        UiState(
            meeting = requireNotNull(savedStateHandle[Const.Meeting])
        )
    )

    private lateinit var myWorkspaceUser: WorkspaceUser
    private var searchJob: Job? = null

    fun uiState() = _uiState.asStateFlow()
    val listState = Info.values()
        .associate { info ->
            info to ""
        }.toList()
        .toMutableStateMap()

    private var defaultOwnerString = ""

    init {
        viewModelScope.launch {
            myWorkspaceUser = getMyWorkSpaceUserUseCase()
            defaultOwnerString = myWorkspaceUser.nickname
            listState[Info.Owners] = defaultOwnerString

            _uiState.update {
                it.copy(
                    meeting = it.meeting.copy(ownerIds = listOf(myWorkspaceUser.workspaceUserId)),
                    teams = getWorkSpaceTeamUseCase()
                )
            }

        }
    }

    fun updateText(info: Info, input: String) {
        listState[info] = input
        checkEssentialField()
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

    private fun checkEssentialField() {
        listState
            .filter { it.key.isEssential }
            .all { it.value.isNotEmpty() }
            .also { isVerify -> _uiState.update { it.copy(isVerify = isVerify) } }
    }

    fun createMeeting(onCreate: (Meeting) -> Unit) {
        viewModelScope.launch {
            val ownerIds = uiState().value.meeting.ownerIds.toLongArray()
            val fixedParticipants = getWorkspaceUserUseCase(*ownerIds)
            onCreate(
                uiState().value.meeting.copy(
                    title = listState[Info.Title].orEmpty(),
                    purpose = listState[Info.Purpose].orEmpty(),
                    participants = fixedParticipants
                )
            )
        }
    }

    fun selectTeam(teamId: Long) {
        listState[Info.Team] = uiState().value.teams.find { it.id == teamId }?.name.orEmpty()
        _uiState.update {
            val team = Team(id = teamId, name = listState[Info.Team].orEmpty())
            it.copy(
                meeting = uiState().value.meeting.copy(team = team)
            )
        }
        checkEssentialField()
    }

    fun selectOwner(owners: List<WorkspaceUser>) {
        listState[Info.Owners] += ", " + owners.joinToString { it.nickname }
        _uiState.update {
            val meeting = uiState().value.meeting
            it.copy(meeting = meeting.copy(ownerIds = meeting.ownerIds + owners.map { it.workspaceUserId }))
        }
    }

    fun clearOwner() {
        listState[Info.Owners] = defaultOwnerString
        _uiState.update {
            it.copy(
                meeting = uiState().value.meeting.copy(ownerIds = listOf(myWorkspaceUser.workspaceUserId)),
                searchedUsers = emptyList()
            )
        }
    }

    data class UiState(
        val meeting: Meeting,
        val isVerify: Boolean = false,
        val teams: List<Team> = emptyList(),
        val searchedUsers: List<WorkspaceUser> = emptyList(),
        val selectedTeamName: String = ""
    )

    sealed interface BottomSheetState {
        object Owner : BottomSheetState
        object Team : BottomSheetState
    }

    sealed interface Event {
        class OnTextChange(val info: Info,val input: String):Event
        object Action : Event
        object Next : Event
        object Previous : Event
        object Load : Event
    }

    enum class Info(
        @StringRes val title: Int,
        val isEssential: Boolean = false,
        val limit: Int = 0,
        val isModal: Boolean,
    ) {
        Title(R.string.meeting_title, true, 30, false),
        Purpose(R.string.meeting_purpose, true, 10, false),
        Owners(R.string.owners, isEssential = false, limit = 0, true),
        Team(R.string.charged_team, isEssential = true, limit = 0, true)
    }

}