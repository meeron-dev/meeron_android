package fourtune.meeron.presentation.ui.calendar

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface Event {
    object Previous : Event
    object Next : Event
}

@HiltViewModel
class CalendarViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _currentDay = MutableStateFlow(CalendarDay.today())
    fun currentDay() = _currentDay.asStateFlow()

    private val _event = MutableSharedFlow<Event>()
    fun event() = _event.asSharedFlow()

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

}