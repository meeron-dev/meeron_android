package fourtune.meeron.presentation.ui.create.information

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.model.Date
import forutune.meeron.domain.model.Time
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class MeetingInfoUiState(
    val date: Date = Date(),
    val startTime: Time = Time(),
    val endTime: Time = Time(),

    )

@HiltViewModel
class CreateMeetingInfoViewModel @Inject constructor(

) : ViewModel() {
    private val _uiState = MutableStateFlow(MeetingInfoUiState())
    private val texts: MutableMap<Int, String> = mutableMapOf()

    fun uiState() = _uiState.asStateFlow()
    fun updateText(ordinal: Int, input: String) {
        texts[ordinal] = input
    }

}