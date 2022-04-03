package fourtune.meeron.presentation.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.Meeting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AgendaDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        UiState(
            meeting = requireNotNull(savedStateHandle.get<Meeting>(Const.Meeting))
        )
    )
    val uiState = _uiState.asStateFlow()

    data class UiState(
        val meeting: Meeting
    )
}