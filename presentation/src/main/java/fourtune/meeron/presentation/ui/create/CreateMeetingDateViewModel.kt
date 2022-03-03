package fourtune.meeron.presentation.ui.create

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.model.Date
import forutune.meeron.domain.usecase.time.DateFormat
import forutune.meeron.domain.usecase.time.GetCurrentDayUseCase
import fourtune.meeron.presentation.ui.create.CreateMeetingDateViewModel.Companion.DisplayDateFormat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class MeetingDateUiState(
    val initialDate: Date = Date(),
    val displayDate: String = DisplayDateFormat
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
            it.copy(initialDate = date)
        }
    }

    fun changeDate(date: Date) {
        _uiState.update {
            it.copy(displayDate = "${date.year}/${date.month}/${date.hourOfDay}")
        }
    }

    companion object {
        const val DisplayDateFormat = "YYYY/MM/DD"
    }
}