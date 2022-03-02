package fourtune.meeron.presentation.ui.create

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.model.Time
import forutune.meeron.domain.usecase.time.GetCurrentDayUseCase
import forutune.meeron.domain.usecase.time.GetTimeUseCase
import fourtune.meeron.presentation.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class UiState(
    val timeMap: Map<Int, Time> = mapOf(
        R.string.start to Time("12:00", "AM"),
        R.string.end to Time("12:00", "PM")
    ),
    val currentDay: String = ""
)

@HiltViewModel
class CreateMeetingTimeViewModel @Inject constructor(
    private val getTimeUseCase: GetTimeUseCase,
    private val getCurrentDayUseCase: GetCurrentDayUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    fun uiState() = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(currentDay = getCurrentDayUseCase())
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
}