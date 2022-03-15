package fourtune.meeron.presentation.ui.calendar.all

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.model.CalendarType
import forutune.meeron.domain.model.YearCount
import forutune.meeron.domain.usecase.meeting.GetYearMeetingCountUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowAllViewModel @Inject constructor(
    private val getYearMeetingCountUseCase: GetYearMeetingCountUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    yearCounts = getYearMeetingCountUseCase(CalendarType.WORKSPACE_USER)
                )
            }
        }
    }

    data class UiState(
        val yearCounts: List<YearCount> = emptyList()
    )
}