package fourtune.meeron.presentation.ui.create.participants

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.model.Date
import forutune.meeron.domain.model.Time
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CreateMeetingParticipantsViewModel @Inject constructor(

) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    data class UiState(
        val title: String = "",
        val date: Date = Date(),
        val startTime: Time = Time(),
        val endTime: Time = Time(),
    )

    sealed interface Event {
        object Action : Event
        object Previous : Event
        object Next : Event
    }
}