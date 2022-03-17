package fourtune.meeron.presentation.ui.calendar.all

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.CalendarType
import forutune.meeron.domain.model.Date
import forutune.meeron.domain.model.MonthCount
import forutune.meeron.domain.model.YearCount
import forutune.meeron.domain.usecase.meeting.GetMonthMeetingCountUseCase
import forutune.meeron.domain.usecase.meeting.GetYearMeetingCountUseCase
import forutune.meeron.domain.usecase.time.DateFormat
import forutune.meeron.domain.usecase.time.GetCurrentDayUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowAllViewModel @Inject constructor(
    private val getYearMeetingCountUseCase: GetYearMeetingCountUseCase,
    private val getMonthMeetingCountUseCase: GetMonthMeetingCountUseCase,
    private val getCurrentDayUseCase: GetCurrentDayUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val calendarType: CalendarType = savedStateHandle[Const.CalendarType] ?: CalendarType.WORKSPACE_USER

    init {
        viewModelScope.launch {
            val yearCounts = getYearMeetingCountUseCase(calendarType)
            val monthCounts = getMonthMeetingCountUseCase(calendarType, yearCounts.first().year)

            val selectedDate = getCurrentDateIfHas(yearCounts, requireNotNull(savedStateHandle[Const.Date]))
            _uiState.update {
                it.copy(
                    yearCounts = yearCounts,
                    monthCounts = monthCounts,
                    selectedDate = selectedDate
                )
            }
        }
    }

    private fun getCurrentDateIfHas(
        yearCounts: List<YearCount>,
        selectedDate: Date
    ): Date {
        val currentDay = getCurrentDayUseCase(DateFormat.YYYYMMDD).date
        val hasSelectedYear = yearCounts.firstOrNull { it.year == selectedDate.year }
        return if (hasSelectedYear != null) currentDay.copy(year = hasSelectedYear.year) else currentDay
    }


    fun loadMonthCounts(year: Int) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    monthCounts = getMonthMeetingCountUseCase(calendarType, year),
                    selectedDate = it.selectedDate.copy(year = year)
                )
            }
        }
    }

    fun selectMonth(month: Int) {
        _uiState.update {
            it.copy(selectedDate = it.selectedDate.copy(month = month))
        }
    }

    data class UiState(
        val yearCounts: List<YearCount> = emptyList(),
        val monthCounts: List<MonthCount> = emptyList(),
        val selectedDate: Date = Date()
    )

    sealed interface Event {
        class ClickYear(val year: Int) : Event
        class ClickMonth(val month: Int) : Event
    }
}