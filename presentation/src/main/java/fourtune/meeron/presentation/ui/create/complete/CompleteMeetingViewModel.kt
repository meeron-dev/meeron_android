package fourtune.meeron.presentation.ui.create.complete

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.model.Date
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CompleteMeetingViewModel @Inject constructor(

) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    data class UiState(
        val title: String = "",
        val date: Date = Date(),
        val owners: String = "",
        val team: String = "",
        val agenda: String = "",
        val participants: String = ""
    )
}