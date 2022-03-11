package fourtune.meeron.presentation.ui.create.time

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.model.Time
import forutune.meeron.domain.usecase.time.GetCurrentTimeUseCase
import forutune.meeron.domain.usecase.time.GetTimeUseCase
import fourtune.meeron.presentation.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class MeetingTimeUiState(
    val timeMap: Map<Int, Time> = mapOf(
        R.string.start to Time("12:00", "AM"),
        R.string.end to Time("12:00", "PM")
    ),
    val meeting: Meeting = Meeting()
)

@HiltViewModel
class CreateMeetingTimeViewModel @Inject constructor(
    private val getTimeUseCase: GetTimeUseCase,
    private val getCurrentTimeUseCase: GetCurrentTimeUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            MeetingTimeUiState(
                meeting = requireNotNull(savedStateHandle[Const.Meeting]),
            )
        )

    fun uiState() = _uiState.asStateFlow()

    init {
        _uiState.update {
            val currentTime = getCurrentTimeUseCase(onTime = true)
            it.copy(
                timeMap = mapOf(
                    R.string.start to currentTime,
                    R.string.end to currentTime
                ),
                meeting = it.meeting.copy(
                    time = "$currentTime ~ $currentTime"
                )
            )
        }
    }

    fun changeTime(key: Int, hour: Int, minute: Int) {
        val time = getTimeUseCase(hour, minute)
        _uiState.update {
            it.copy(
                timeMap = it.timeMap.toMutableMap().apply {
                    this[key] = time
                },
                meeting = it.meeting.copy(
                    time = "${it.timeMap[R.string.start]} ~ ${it.timeMap[R.string.end]}"
                )
            )
        }
    }

    sealed interface Event {
        class ChangeTime(val key: Int, val hour: Int, val minute: Int) : Event
        object Previous : Event
        object Next : Event
        object Exit : Event
    }
}