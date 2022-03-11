package fourtune.meeron.presentation.ui.create.complete

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.usecase.CreateMeetingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompleteMeetingViewModel @Inject constructor(
    private val createMeetingUseCase: CreateMeetingUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState(Meeting()))
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(meeting = requireNotNull(savedStateHandle[Const.Meeting]))
            }
        }
    }

    fun createMeeting() {
        viewModelScope.launch {
            createMeetingUseCase(_uiState.value.meeting)
        }
    }

    data class UiState(
        val meeting: Meeting
    )
}