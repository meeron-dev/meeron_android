package fourtune.meeron.presentation.ui.create.information

import androidx.annotation.StringRes
import androidx.compose.runtime.toMutableStateMap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.Meeting
import fourtune.meeron.presentation.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateMeetingInfoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        UiState(
            date = savedStateHandle.get<String>(Const.Date).orEmpty(),
            time = savedStateHandle.get<String>(Const.Time).orEmpty()
        )
    )
    private var searchJob: Job? = null
    fun uiState() = _uiState.asStateFlow()

    val listState = Info.values()
        .associate { info ->
            info to ""
        }.toList()
        .toMutableStateMap()

    fun updateText(info: Info, input: String) {
        listState[info] = input
        checkEssentialField()
    }

    fun clickModal(info: Info) {
        listState[info] = "asdasd"
        checkEssentialField()
    }

    fun onSearch(text: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            //todo searchUseCase(text)
        }
    }

    private fun checkEssentialField() {
        listState
            .filter { it.key.isEssential }
            .all { it.value.isNotEmpty() }
            .also { isVerify -> _uiState.update { it.copy(isVerify = isVerify) } }
    }

    fun createMeeting(onCreate: (Meeting) -> Unit) {
        viewModelScope.launch {
            onCreate(
                Meeting(
                    title = listState[Info.Title].orEmpty(),
                    date = _uiState.value.date,
                    time = _uiState.value.time,
                    personality = listState[Info.Personality].orEmpty(),
                    owner = listState[Info.Owners].orEmpty(),
                    team = listState[Info.Team].orEmpty(),//todo List<String> 변경 유무 확인
                    agenda = emptyList(),//todo List<String> 변경 유무 확인
                    participants = emptyList()//todo List<String> 변경 유무 확인
                )
            )
        }
    }

    data class UiState(
        val date: String,
        val time: String,
        val isVerify: Boolean = false
    )

    sealed interface BottomSheetState {
        object Owner : BottomSheetState
        object Team : BottomSheetState
    }

    sealed interface Event {
        object Next : Event
        object Previous : Event
        object Load : Event
    }

    enum class Info(
        @StringRes val title: Int,
        val isEssential: Boolean = false,
        val limit: Int = 0,
        val isModal: Boolean,
    ) {
        Title(R.string.meeting_title, true, 30, false),
        Personality(R.string.meeting_personality, true, 10, false),
        Owners(R.string.public_owners, isEssential = false, limit = 0, true),
        Team(R.string.charged_team, isEssential = true, limit = 0, true)
    }

}