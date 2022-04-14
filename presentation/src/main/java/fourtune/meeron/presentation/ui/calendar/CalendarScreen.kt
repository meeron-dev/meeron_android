package fourtune.meeron.presentation.ui.calendar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import forutune.meeron.domain.model.Date
import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.model.WorkSpaceInfo
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.calendar.decorator.EventDecorator
import fourtune.meeron.presentation.ui.calendar.decorator.SelectionDecorator
import fourtune.meeron.presentation.ui.common.text.CircleBackgroundText
import fourtune.meeron.presentation.ui.theme.MeeronTheme
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    showAll: (Date) -> Unit = {},
    goToMeetingDetail: (Meeting) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text(
                        text = uiState.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.black)
                    )
                }
            )
        }
    ) {
        CalendarScreen(
            uiState = uiState,
            topBarEvent = viewModel.topBarEvent,
            event = { event ->
                when (event) {
                    CalendarViewModel.Event.Next -> viewModel.goToNext()
                    CalendarViewModel.Event.Previous -> viewModel.goToPrevious()
                    is CalendarViewModel.Event.ChangeMonth -> viewModel.changeMonth(event.date)
                    is CalendarViewModel.Event.ChangeDay -> viewModel.changeDay(event.date)
                    is CalendarViewModel.Event.ShowAll -> showAll(event.date)
                    is CalendarViewModel.Event.SelectMeeting -> goToMeetingDetail(event.meeting)
                }
            }
        )
    }
}

@Composable
private fun CalendarScreen(
    uiState: CalendarViewModel.UiState = CalendarViewModel.UiState(),
    topBarEvent: SharedFlow<CalendarViewModel.TopBarEvent> = MutableSharedFlow(),
    event: (CalendarViewModel.Event) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .background(colorResource(id = R.color.background))
            .fillMaxSize()
            .padding(top = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CalendarContents(
            selectedDay = uiState.selectedDay,
            days = uiState.days,
            topBarEvent = topBarEvent,
            event = event
        )
        Divider(color = colorResource(id = R.color.white), thickness = 14.dp)
        SelectedMeetings(
            selectedDay = uiState.selectedDay,
            selectedMeetings = uiState.selectedMeetings,
            myWorkSpaceId = uiState.myWorkSpaceId,
            event = event
        )
    }
}

@Composable
fun CalendarContents(
    selectedDay: Date,
    days: List<Int>,
    topBarEvent: SharedFlow<CalendarViewModel.TopBarEvent>,
    event: (CalendarViewModel.Event) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            CalendarTitle(
                selectedDay = selectedDay,
                previousEvent = { event(CalendarViewModel.Event.Previous) },
                nextEvent = { event(CalendarViewModel.Event.Next) }
            )
        }
        Text(
            modifier = Modifier
                .clickable(onClick = { event(CalendarViewModel.Event.ShowAll(selectedDay)) })
                .align(Alignment.End)
                .padding(horizontal = 20.dp, vertical = 10.dp),
            text = buildAnnotatedString {
                val text = stringResource(R.string.show_all)
                append(text)
                addStyle(SpanStyle(textDecoration = TextDecoration.Underline), 0, text.length)
            },
            fontSize = 13.sp,
            color = colorResource(id = R.color.dark_primary)
        )
        Calendar(selectedDay, days, topBarEvent, event)
    }
}

@Composable
fun SelectedMeetings(
    selectedDay: Date,
    selectedMeetings: List<Pair<Meeting, WorkSpaceInfo?>>,
    myWorkSpaceId: Long,
    event: (CalendarViewModel.Event) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "${selectedDay.month}월 ${selectedDay.hourOfDay}일",
            fontSize = 19.sp,
            color = colorResource(id = R.color.black)
        )
        Spacer(modifier = Modifier.padding(2.dp))
        Text(
            text = "${selectedMeetings.size}개의 회의가 존재합니다.",
            fontSize = 13.sp,
            color = colorResource(id = R.color.dark_gray)
        )
        LazyColumn(
            contentPadding = PaddingValues(vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(11.dp)
        ) {
            itemsIndexed(selectedMeetings) { index, (meeting, info) ->
                CalendarDetail(
                    index = index,
                    meeting = meeting,
                    info = info,
                    myWorkSpaceId = myWorkSpaceId,
                    event = event
                )
            }
        }
    }

}

@Composable
private fun Calendar(
    selectedDay: Date,
    days: List<Int>,
    topBarEvent: SharedFlow<CalendarViewModel.TopBarEvent>,
    event: (CalendarViewModel.Event) -> Unit,
) {

    val scope = rememberCoroutineScope()
    val decorators = rememberSaveable {
        mutableListOf<DayViewDecorator>()
    }
    AndroidView(factory = { context ->
        val selectionDecor by lazy { SelectionDecorator(context) }

        MaterialCalendarView(context).apply {
            if (selectedDay != Date.EMPTY) {
                currentDate = CalendarDay.from(selectedDay.year, selectedDay.month, selectedDay.hourOfDay)
            }
            scope.launch {
                topBarEvent.collectLatest {
                    when (it) {
                        CalendarViewModel.TopBarEvent.Next -> goToNext()
                        CalendarViewModel.TopBarEvent.Previous -> goToPrevious()
                    }
                }
            }

            topbarVisible = false
            setDateTextAppearance(R.style.CalendarTextAppearance)
            setWeekDayTextAppearance(R.style.CalendarWeekAppearance)
            setBackgroundColor(ContextCompat.getColor(context, R.color.background))

            addDecorators(selectionDecor)
            setOnDateChangedListener { widget, date, selected ->
                selectionDecor.setDate(date)
                event(CalendarViewModel.Event.ChangeDay(Date(date.year, date.month, date.day)))
                widget.invalidateDecorators()
            }
            setOnMonthChangedListener { _, date ->
                event(CalendarViewModel.Event.ChangeMonth(Date(date.year, date.month, date.day)))
            }

        }
    }, update = { calendarView ->
        decorators.forEach(calendarView::removeDecorator)
        decorators.clear()

        if (days.isNotEmpty()) {
            days.forEach {
                decorators.add(
                    EventDecorator(
                        calendarView.context,
                        CalendarDay.from(
                            selectedDay.year,
                            selectedDay.month,
                            it
                        )
                    )
                )
            }
        }
        calendarView.addDecorators(decorators)
    })
}

@Composable
private fun CalendarTitle(
    selectedDay: Date,
    previousEvent: () -> Unit,
    nextEvent: () -> Unit
) {
    IconButton(onClick = previousEvent) {
        Image(painter = painterResource(id = R.drawable.ic_left_arrow), contentDescription = null)
    }
    Column(
        modifier = Modifier.padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${selectedDay.year}",
            fontSize = 16.sp,
            color = colorResource(id = R.color.dark_gray)
        )
        Text(
            text = String.format(stringResource(id = R.string.calendar_month), selectedDay.month),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.black)
        )
    }
    IconButton(onClick = nextEvent) {
        Image(painter = painterResource(id = R.drawable.ic_right_arrow_24), contentDescription = null)
    }
}

@Composable
fun CalendarDetail(
    index: Int = 0,
    meeting: Meeting,
    info: WorkSpaceInfo? = null,
    myWorkSpaceId: Long = -1,
    event: (CalendarViewModel.Event) -> Unit = {}
) {
    Row(
        modifier = Modifier.clickable(
            interactionSource = MutableInteractionSource(),
            indication = rememberRipple(color = colorResource(id = R.color.primary)),
            onClick = { event(CalendarViewModel.Event.SelectMeeting(meeting)) },
            enabled = myWorkSpaceId == info?.workSpaceId
        )
    ) {
        CircleBackgroundText(
            modifier = Modifier
                .background(colorResource(id = R.color.primary))
                .size(24.dp),
            "${index + 1}",
            colorResource(id = R.color.white)
        )
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = meeting.meetingName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.dark_primary)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = meeting.time, fontSize = 12.sp, color = colorResource(id = R.color.medium_primary))
                if (myWorkSpaceId != info?.workSpaceId) {
                    Text(
                        text = info?.workSpaceName.orEmpty(),
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.medium_primary),
                    )
                } else {
                    Image(painter = painterResource(id = R.drawable.ic_right_arrow_24), contentDescription = null)
                }
            }
        }
    }
}

@Preview
@Composable
private fun CalendarDetailPrev() {
    MeeronTheme {
        Column {
            CalendarDetail(meeting = Meeting(), info = WorkSpaceInfo(-1, "meeron"))
            CalendarDetail(meeting = Meeting())
        }
    }
}

@Preview
@Composable
private fun CalendarPrev() {
    MeeronTheme {
        CalendarScreen(uiState = CalendarViewModel.UiState())
    }
}