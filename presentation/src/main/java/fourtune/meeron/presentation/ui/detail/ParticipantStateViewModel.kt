package fourtune.meeron.presentation.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.model.MeetingState
import forutune.meeron.domain.usecase.meeting.ChangeMeetingStateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ParticipantStateViewModel @Inject constructor(
    private val changeMeetingState: ChangeMeetingStateUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _meeting = MutableStateFlow(requireNotNull(savedStateHandle.get<Meeting>(Const.Meeting)))
    val meeting = _meeting.asStateFlow()

    fun changeState(state: MeetingState, onComplete: () -> Unit) {
        viewModelScope.launch {
            runCatching {
                changeMeetingState(meetingId = meeting.value.meetingId, state)
            }.onSuccess {
                onComplete()
            }.onFailure { 
                Timber.tag("🔥zero:changeState").e("$it")
            }
        }
    }

}