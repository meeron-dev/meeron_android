package fourtune.meeron.presentation.ui.create.complete

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.usecase.CreateMeetingUseCase
import forutune.meeron.domain.usecase.GetUserUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CompleteMeetingViewModel @Inject constructor(
    private val createMeetingUseCase: CreateMeetingUseCase,
    getUserUseCase: GetUserUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState(requireNotNull(savedStateHandle[Const.Meeting])))
    val uiState = _uiState.asStateFlow()

    private val _toast = MutableSharedFlow<String>()
    val toast = _toast.asSharedFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                val workspaceUserIds = it.meeting.ownerIds.firstOrNull()
                val owners =
                    if (workspaceUserIds != null) getUserUseCase.invoke(workspaceUserIds = longArrayOf(workspaceUserIds))
                    else emptyList()
                it.copy(
                    owners = String.format("${owners.firstOrNull()?.nickname} 외 %d명", it.meeting.ownerIds.size - 1)
                )
            }
        }
    }

    fun createMeeting(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            runCatching {
                createMeetingUseCase(_uiState.value.meeting)
            }.onFailure {
                Timber.tag("🔥zero:createMeeting").e("$it")
                _toast.emit("회의 생성에 실패했습니다. ${it.message}")
            }.onSuccess {
                onComplete()
            }
        }
    }

    data class UiState(
        val meeting: Meeting,
        val owners: String = ""
    )
}