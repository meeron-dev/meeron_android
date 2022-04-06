package fourtune.meeron.presentation.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.Agenda
import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.usecase.meeting.agenda.GetAgendaUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgendaDetailViewModel @Inject constructor(
    private val getAgendaUseCase: GetAgendaUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        UiState(
            meeting = requireNotNull(savedStateHandle.get<Meeting>(Const.Meeting))
        )
    )

    val uiState = _uiState.asStateFlow()

    fun updateAgenda(selected: Int) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedAgenda = getAgendaUseCase(uiState.value.meeting.meetingId, selected)
                )
            }
        }
    }


    data class UiState(
        val meeting: Meeting,
        val selectedAgenda: Agenda = Agenda()
    )
}