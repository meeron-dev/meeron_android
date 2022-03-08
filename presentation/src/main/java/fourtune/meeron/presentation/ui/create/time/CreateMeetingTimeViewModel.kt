package fourtune.meeron.presentation.ui.create.time

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.model.Time
import forutune.meeron.domain.usecase.time.DateFormat
import forutune.meeron.domain.usecase.time.GetCurrentDayUseCase
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
    val currentDay: String = ""
)

@HiltViewModel
class CreateMeetingTimeViewModel @Inject constructor(
    private val getTimeUseCase: GetTimeUseCase,
    private val getCurrentTimeUseCase: GetCurrentTimeUseCase,
    private val getCurrentDayUseCase: GetCurrentDayUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MeetingTimeUiState())
    fun uiState() = _uiState.asStateFlow()

    init {
        _uiState.update {
            val currentTime = getCurrentTimeUseCase(onTime = true)
            it.copy(
                timeMap = mapOf(
                    R.string.start to currentTime,
                    R.string.end to currentTime
                ),
                currentDay = getCurrentDayUseCase(DateFormat.SimpleString).string
            )
        }
    }

    fun changeTime(key: Int, hour: Int, minute: Int) {
        val time = getTimeUseCase(hour, minute)
        _uiState.update {
            it.copy(
                timeMap = it.timeMap.toMutableMap().apply {
                    this[key] = time
                }
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