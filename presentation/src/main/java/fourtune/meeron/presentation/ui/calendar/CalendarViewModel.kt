package fourtune.meeron.presentation.ui.calendar

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.usecase.meeting.GetTodayMeetingUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getTodayMeetingUseCase: GetTodayMeetingUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _currentDay = MutableStateFlow(CalendarDay.today())
    fun currentDay() = _currentDay.asStateFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<Event>()
    fun event() = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    todayMeetings = getTodayMeetingUseCase()
                )
            }

        }
    }

    fun changeDay(day: CalendarDay) {
        _currentDay.update { day }
    }

    fun goToPrevious() {
        viewModelScope.launch {
            _event.emit(Event.Previous)
        }
    }

    fun goToNext() {
        viewModelScope.launch {
            _event.emit(Event.Next)
        }
    }

    data class UiState(
        val todayMeetings: List<Meeting> = emptyList()
    )

    sealed interface Event {
        object Previous : Event
        object Next : Event
        class Change(val day: CalendarDay) : Event
        object ShowAll : Event
    }

}