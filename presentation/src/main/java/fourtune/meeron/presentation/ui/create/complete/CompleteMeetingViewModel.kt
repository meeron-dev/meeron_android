package fourtune.meeron.presentation.ui.create.complete

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.model.Meeting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CompleteMeetingViewModel @Inject constructor(

) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState(Meeting()))
    val uiState = _uiState.asStateFlow()

    data class UiState(
        val meeting: Meeting
    )
}