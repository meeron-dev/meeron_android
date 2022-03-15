package fourtune.meeron.presentation.ui.calendar

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.CalendarType
import forutune.meeron.domain.model.Date
import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.usecase.meeting.GetDateMeetingUseCase
import forutune.meeron.domain.usecase.meeting.GetTodayMeetingUseCase
import forutune.meeron.domain.usecase.time.DateFormat
import forutune.meeron.domain.usecase.time.GetCurrentDayUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getTodayMeetingUseCase: GetTodayMeetingUseCase,
    private val getDateMeetingUseCase: GetDateMeetingUseCase,
    private val getCurrentDayUseCase: GetCurrentDayUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val calendarType: CalendarType = savedStateHandle[Const.CalendarType] ?: CalendarType.WORKSPACE_USER

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<Event>()
    fun event() = _event.asSharedFlow()

    private val _topBarEvent = MutableSharedFlow<TopBarEvent>()
    val topBarEvent = _topBarEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            val currentDay = getCurrentDayUseCase(DateFormat.YYYYMMDD).date
            _uiState.update {
                it.copy(
                    selectedDay = currentDay,
                    todayMeetings = getTodayMeetingUseCase(),
                    days = getDateMeetingUseCase(
                        calendarType,
                        currentDay
                    ),
                )
            }

        }
    }

    fun changeDay(day: Date) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedDay = day,
                    days = getDateMeetingUseCase(calendarType, day)
                )
            }
        }
    }

    fun goToPrevious() {
        viewModelScope.launch {
            _topBarEvent.emit(TopBarEvent.Previous)
        }
    }

    fun goToNext() {
        viewModelScope.launch {
            _topBarEvent.emit(TopBarEvent.Next)
        }
    }

    data class UiState(
        val todayMeetings: List<Meeting> = emptyList(),
        val days: List<Int> = emptyList(),
        val selectedDay: Date = Date()
    )

    sealed interface Event {
        object Previous : Event
        object Next : Event
        class Change(val day: Date) : Event
        object ShowAll : Event
    }

    sealed interface TopBarEvent {
        object Previous : TopBarEvent
        object Next : TopBarEvent
    }

}