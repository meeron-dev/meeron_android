package fourtune.meeron.presentation.ui.create

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.model.Date
import forutune.meeron.domain.usecase.time.DateFormat
import forutune.meeron.domain.usecase.time.GetCurrentDayUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class MeetingDateUiState(
    val initialDate: Date = Date(),
    val displayDate: String = "YY/MM/DD"
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
            val parser = SimpleDateFormat("yyyy/MM/DD", Locale.US)
            val formatter = SimpleDateFormat("yy/MM/DD", Locale.US)
            val result = formatter.format(parser.parse("${date.year}/${date.month}/${date.hourOfDay}"))
            it.copy(displayDate = result)
        }
    }
}