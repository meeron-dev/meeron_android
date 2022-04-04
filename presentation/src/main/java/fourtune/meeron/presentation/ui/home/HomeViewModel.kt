package fourtune.meeron.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.usecase.meeting.GetTodayMeetingUseCase
import forutune.meeron.domain.usecase.workspace.GetLatestWorkspaceIdUseCase
import forutune.meeron.domain.usecase.workspace.GetWorkSpaceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTodayMeetingUseCase: GetTodayMeetingUseCase,
    private val getLatestWorkspaceIdUseCase: GetLatestWorkspaceIdUseCase,
    private val getWorkSpaceUseCase: GetWorkSpaceUseCase,
) : ViewModel() {
    private val _currentDay = MutableStateFlow(CalendarDay.today())
    fun currentDay() = _currentDay.asStateFlow()
    private val _uiState = MutableStateFlow(UiState())

    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val workspaceId = getLatestWorkspaceIdUseCase()
            val workspace = getWorkSpaceUseCase(workspaceId = workspaceId)
            _uiState.update {
                it.copy(
                    workspaceName = workspace.workSpaceName,
                )
            }
            fetch()
        }
    }

    fun fetch() {
        viewModelScope.launch {
            _uiState.update {
                val todayMeeting = getTodayMeetingUseCase()
                it.copy(
                    todayMeeting = todayMeeting
                )
            }
        }
    }

    data class UiState(
        val workspaceName: String = "",
        val todayMeeting: List<Meeting> = emptyList()
    )

}