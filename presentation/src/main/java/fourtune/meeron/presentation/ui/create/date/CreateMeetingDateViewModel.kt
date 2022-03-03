package fourtune.meeron.presentation.ui.create

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.model.Date
import forutune.meeron.domain.usecase.time.DateFormat
import forutune.meeron.domain.usecase.time.GetCurrentDayUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class MeetingDateUiState(
    val date: Date = Date()
)

@HiltViewModel
class CreateMeetingDateViewModel @Inject constructor(
    private val getCurrentDayUseCase: GetCurrentDayUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(MeetingDateUiState())
    fun uiState() = _uiState.asStateFlow()

    init {
        _uiState.update {
            val date = getCurrentDayUseCase(DateFormat.YYYYMMDD).date
            it.copy(date = date.copy(month = date.month))
        }
    }

    fun changeDate(date: Date) {
        _uiState.update {
            it.copy(date = date)
        }
    }

}