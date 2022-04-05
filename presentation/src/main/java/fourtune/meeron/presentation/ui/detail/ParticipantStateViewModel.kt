package fourtune.meeron.presentation.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.model.MeetingState
import forutune.meeron.domain.usecase.meeting.ChangeMeetingStateUseCase
import forutune.meeron.domain.usecase.workspace.GetCurrentWorkspaceInfoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ParticipantStateViewModel @Inject constructor(
    private val changeMeetingState: ChangeMeetingStateUseCase,
    private val getCurrentWorkspaceInfo: GetCurrentWorkspaceInfoUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        UiState(meeting = requireNotNull(savedStateHandle.get<Meeting>(Const.Meeting)))
    )
    val uiState = _uiState.asStateFlow()

    private val _meeting = MutableStateFlow(requireNotNull(savedStateHandle.get<Meeting>(Const.Meeting)))
    val meeting = _meeting.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(workspaceName = getCurrentWorkspaceInfo().workSpaceName)
            }
        }
    }

    fun changeState(state: MeetingState, onComplete: () -> Unit) {
        viewModelScope.launch {
            runCatching {
                changeMeetingState(meetingId = meeting.value.meetingId, state)
            }.onSuccess {
                onComplete()
            }.onFailure {
                Timber.tag("🔥zero:changeState").e("$it")
            }
        }
    }

    data class UiState(
        val meeting: Meeting,
        val workspaceName: String = ""
    )

}