package fourtune.meeron.presentation.ui.create.information

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
import forutune.meeron.domain.usecase.SearchUseCase
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
    private val searchUseCase: SearchUseCase,
    getWorkSpaceTeamUseCase: GetWorkSpaceTeamUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        UiState(
            meeting = Meeting(
                date = savedStateHandle.get<String>(Const.Date).orEmpty(),
                time = savedStateHandle.get<String>(Const.Time).orEmpty()
            ),
        )
    )

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    teams = getWorkSpaceTeamUseCase()
                )
            }
        }
    }

    private var searchJob: Job? = null
    fun uiState() = _uiState.asStateFlow()

    val listState = Info.values()
        .associate { info ->
            info to ""
        }.toList()
        .toMutableStateMap()

    fun updateText(info: Info, input: String) {
        listState[info] = input
        checkEssentialField()
    }

    fun onSearch(text: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            _uiState.update {
                it.copy(searchedUsers = searchUseCase.invoke(text))
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
            onCreate(
                uiState().value.meeting.copy(
                    title = listState[Info.Title].orEmpty(),
                    purpose = listState[Info.Purpose].orEmpty(),
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
        listState[Info.Owners] = owners.joinToString { it.nickname }
        _uiState.update {
            it.copy(meeting = uiState().value.meeting.copy(ownerIds = owners.map { it.workspaceUserId }))
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
        Owners(R.string.public_owners, isEssential = false, limit = 0, true),
        Team(R.string.charged_team, isEssential = true, limit = 0, true)
    }

}