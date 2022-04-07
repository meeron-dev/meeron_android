package fourtune.meeron.presentation.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.prolificinteractive.materialcalendarview.CalendarDay
import forutune.meeron.domain.model.Meeting
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.StateItem
import fourtune.meeron.presentation.ui.common.topbar.CenterTextTopAppBar
import fourtune.meeron.presentation.ui.theme.MeeronTheme

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    bottomBarSize: Dp = 90.dp,
    openCalendar: () -> Unit,
    onClickMeeting: (meeting: Meeting) -> Unit
) {
    val currentDay by homeViewModel.currentDay().collectAsState()
    val uiState by homeViewModel.uiState.collectAsState()
    val pagerState = rememberPagerState(0)

    HomeScreen(
        bottomBarSize = bottomBarSize,
        pagerState = pagerState,
        currentDay = currentDay,
        uiState = uiState,
        openCalendar = openCalendar,
        onClickMeeting = onClickMeeting
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun HomeScreen(
    bottomBarSize: Dp = 90.dp,
    pagerState: PagerState = rememberPagerState(0),
    currentDay: CalendarDay = CalendarDay.today(),
    uiState: HomeViewModel.UiState,
    openCalendar: () -> Unit = {},
    onClickMeeting: (meeting: Meeting) -> Unit = {}
) {
    Column(
        Modifier
            .fillMaxHeight()
            .padding(bottom = bottomBarSize + 16.dp)
    ) {
        CalendarTitle(
            modifier = Modifier.padding(vertical = 28.dp, horizontal = 4.dp),
            date = currentDay,
            openCalendar = openCalendar
        )
        Text(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = buildAnnotatedString {
                append("오늘의 회의  ")
                withStyle(SpanStyle(colorResource(id = R.color.primary), fontWeight = FontWeight.Bold)) {
                    append("${uiState.todayMeeting.size}")
                }
            },
            color = colorResource(id = R.color.dark_gray),
            fontSize = 15.sp,
            lineHeight = 24.sp
        )
        Spacer(modifier = Modifier.padding(6.dp))
        if (uiState.todayMeeting.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.not_found_meeting),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = colorResource(id = R.color.gray)
                )
            }
        } else {
            HorizontalPager(
                modifier = Modifier.fillMaxHeight(),
                count = uiState.todayMeeting.size,
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 50.dp),
                itemSpacing = 14.dp
            ) { pager ->
                repeat(uiState.todayMeeting.size) {
                    PagerItem(
                        meeting = uiState.todayMeeting[pager],
                        onClick = { onClickMeeting(uiState.todayMeeting[pager]) }
                    )
                }
            }
        }

    }
}

@Composable
private fun PagerItem(meeting: Meeting, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxHeight()
            .clickable(onClick = onClick), elevation = 1.dp
    ) {
        //content
        Column(
            modifier = Modifier
                .padding(top = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = meeting.meetingName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.black),
                    overflow = TextOverflow.Clip
                )
                Spacer(modifier = Modifier.padding(3.dp))
                Text(
                    text = "${meeting.date.displayString()}\n${meeting.time}",
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.dark_gray)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.meeting_plan),
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.gray)
                    )
                    Text(text = "주관 ${meeting.team.name}", fontSize = 13.sp, color = colorResource(id = R.color.gray))
                }
                Column {
                    StateItem(resource = R.drawable.ic_circle, count = "${meeting.attends}")
                    StateItem(resource = R.drawable.ic_x, count = "${meeting.absents}")
                    StateItem(resource = R.drawable.ic_qeustion_mark, count = "${meeting.unknowns}")
                }
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Box(
                modifier = Modifier
                    .background(color = colorResource(id = R.color.primary))
                    .fillMaxWidth()
                    .padding(vertical = 34.dp, horizontal = 24.dp)
            ) {
                Text(text = meeting.agenda.firstOrNull()?.name.orEmpty(), fontSize = 14.sp)
            }
        }

    }

}


@Composable
fun HomeTopBar(
    uiState: HomeViewModel.UiState,
    addMeeting: () -> Unit
) {
//    val scope = rememberCoroutineScope()
    CenterTextTopAppBar(
        actionIcon = painterResource(id = R.drawable.ic_plus),
        onAction = addMeeting,
        text = {TitleText(Modifier.fillMaxWidth(), uiState.workspaceName)}
//        navigationIcon = painterResource(id = R.drawable.ic_home_menu),
//        onNavigation = {
//            if (scaffoldState.drawerState.isOpen) {
//                scope.launch { scaffoldState.drawerState.close() }
//            } else {
//                scope.launch { scaffoldState.drawerState.open() }
//            }
//        }
    )
}

@Composable
private fun TitleText(modifier: Modifier, title: String) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CalendarTitle(modifier: Modifier, date: CalendarDay, openCalendar: () -> Unit = {}) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = openCalendar) {
            Image(painter = painterResource(id = R.drawable.ic_calendar), contentDescription = null)
        }
        Text(
            text = String.format(stringResource(R.string.calendar_title), date.month, date.day),
            fontSize = 22.sp,
            color = colorResource(id = R.color.dark_gray)
        )
    }
}


@OptIn(ExperimentalPagerApi::class)
@Preview
@Composable
private fun PagerItemPrev() {
    MeeronTheme {
        HomeScreen(uiState = HomeViewModel.UiState("workspaceName"))
    }
}
