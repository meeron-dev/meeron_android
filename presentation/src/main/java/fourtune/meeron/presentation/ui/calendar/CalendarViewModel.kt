package fourtune.meeron.presentation.ui.calendar

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.*
import forutune.meeron.domain.usecase.me.GetMyWorkSpaceUserUseCase
import forutune.meeron.domain.usecase.meeting.date.GetDateMeetingCountUseCase
import forutune.meeron.domain.usecase.meeting.date.GetDateMeetingUseCase
import forutune.meeron.domain.usecase.time.DateFormat
import forutune.meeron.domain.usecase.time.GetCurrentDayUseCase
import forutune.meeron.domain.usecase.workspace.GetCurrentWorkspaceInfoUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getMyWorkSpaceUserUseCase: GetMyWorkSpaceUserUseCase,
    private val getDateMeetingCountUseCase: GetDateMeetingCountUseCase,
    private val getCurrentDayUseCase: GetCurrentDayUseCase,
    private val getDateMeetingUseCase: GetDateMeetingUseCase,
    private val currentWorkspaceInfo: GetCurrentWorkspaceInfoUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val calendarType: CalendarType = savedStateHandle[Const.CalendarType] ?: CalendarType.WORKSPACE_USER

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<Event>()
    fun event() = _event.asSharedFlow()

    private val _topBarEvent = MutableSharedFlow<TopBarEvent>()
    val topBarEvent = _topBarEvent.asSharedFlow()


    init {
        viewModelScope.launch {
            val currentDay = getCurrentDayUseCase(DateFormat.YYYYMMDD).date
            _uiState.update {
                it.copy(
                    selectedDay = currentDay,
                    days = getDateMeetingCountUseCase(
                        calendarType,
                        currentDay
                    ),
                    myWorkSpaceId = getMyWorkSpaceUserUseCase().workspaceId,
                    selectedMeetings = getDateMeetingUseCase(calendarType, date = currentDay),
                    title = when (calendarType) {
                        CalendarType.WORKSPACE -> currentWorkspaceInfo().workSpaceName
                        CalendarType.TEAM -> savedStateHandle.get<Team>(Const.Team)?.name.orEmpty()
                        CalendarType.WORKSPACE_USER -> "나의 캘린더"
                    }
                )
            }

        }
    }

    fun changeDay(date: Date) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedDay = date,
                    selectedMeetings = getDateMeetingUseCase(calendarType, date)
                )
            }
        }
    }

    fun changeMonth(date: Date) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedDay = date,
                    days = getDateMeetingCountUseCase(calendarType, date)
                )
            }
        }
    }

    fun goToPrevious() {
        viewModelScope.launch {
            _topBarEvent.emit(TopBarEvent.Previous)
        }
    }

    fun goToNext() {
        viewModelScope.launch {
            _topBarEvent.emit(TopBarEvent.Next)
        }
    }

    data class UiState(
        val selectedMeetings: List<Pair<Meeting, WorkSpaceInfo?>> = emptyList(),
        val days: List<Int> = emptyList(),
        val selectedDay: Date = Date(),
        val myWorkSpaceId: Long = -1,
        val title: String = ""
    )

    sealed interface Event {
        object Previous : Event
        object Next : Event
        class ChangeMonth(val date: Date) : Event
        class ChangeDay(val date: Date) : Event
        class ShowAll(val date: Date) : Event
        class SelectMeeting(val meeting: Meeting) : Event
    }

    sealed interface TopBarEvent {
        object Previous : TopBarEvent
        object Next : TopBarEvent
    }

}