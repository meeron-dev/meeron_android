package fourtune.meeron.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.usecase.meeting.GetTodayMeetingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTodayMeetingUseCase: GetTodayMeetingUseCase
) : ViewModel() {
    private val _currentDay = MutableStateFlow(CalendarDay.today())
    fun currentDay() = _currentDay.asStateFlow()

    init {
        viewModelScope.launch {
            getTodayMeetingUseCase().also {
                Timber.tag("🔥zero:init").d("$it")
            }
        }
    }

}