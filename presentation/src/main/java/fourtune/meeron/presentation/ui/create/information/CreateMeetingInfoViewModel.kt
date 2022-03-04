package fourtune.meeron.presentation.ui.create.information

import androidx.compose.runtime.toMutableStateMap
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.model.Date
import forutune.meeron.domain.model.Time
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CreateMeetingInfoViewModel @Inject constructor(

) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    fun uiState() = _uiState.asStateFlow()

    val listState = Info.values()
        .associate { info ->
            info to ""
        }.toList()
        .toMutableStateMap()

    fun updateText(info: Info, input: String) {
        listState[info] = input
    }

    fun clickModal(info: Info) {
        listState[info] = "asdasd"
    }

    data class UiState(
        val date: Date = Date(),
        val startTime: Time = Time(),
        val endTime: Time = Time(),
    )

    sealed interface BottomSheetState {
        object Owner : BottomSheetState
        object Team : BottomSheetState
    }
}