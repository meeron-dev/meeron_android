package fourtune.meeron.presentation.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import com.google.accompanist.pager.rememberPagerState
import com.prolificinteractive.materialcalendarview.CalendarDay
import forutune.meeron.domain.model.Meeting
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.theme.MeeronTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    bottomBarSize: Dp,
    openCalendar: () -> Unit,
    uiState: HomeViewModel.UiState
) {
    val currentDay by homeViewModel.currentDay().collectAsState()
    val pagerState = rememberPagerState(0)
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
                    PagerItem(meeting = uiState.todayMeeting[pager])
                }
            }
        }

    }
}

@Composable
private fun PagerItem(meeting: Meeting) {
    Card(modifier = Modifier.fillMaxHeight(), elevation = 1.dp) {
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
                    text = meeting.title,
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
                    Row {
                        Image(painter = painterResource(id = R.drawable.ic_circle), contentDescription = null)
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(text = "5", color = colorResource(id = R.color.gray), fontSize = 12.sp)
                    }
                    Row {
                        Image(painter = painterResource(id = R.drawable.ic_triangle), contentDescription = null)
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(text = "5", color = colorResource(id = R.color.gray), fontSize = 12.sp)
                    }
                    Row {
                        Image(painter = painterResource(id = R.drawable.ic_x), contentDescription = null)
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(text = "5", color = colorResource(id = R.color.gray), fontSize = 12.sp)
                    }
                    Row {
                        Image(painter = painterResource(id = R.drawable.ic_qeustion_mark), contentDescription = null)
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(text = "5", color = colorResource(id = R.color.gray), fontSize = 12.sp)
                    }
                    //o x ?
                }
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Box(
                modifier = Modifier
                    .background(color = colorResource(id = R.color.primary))
                    .fillMaxWidth()
                    .padding(vertical = 34.dp, horizontal = 24.dp)
            ) {
                Text(text = meeting.purpose, fontSize = 14.sp)
            }
        }

    }

}


@Composable
fun HomeTopBar(
    scaffoldState: ScaffoldState,
    uiState: HomeViewModel.UiState,
    addMeeting: () -> Unit
) {
    val scope = rememberCoroutineScope()

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                if (scaffoldState.drawerState.isOpen) {
                    scope.launch { scaffoldState.drawerState.close() }
                } else {
                    scope.launch { scaffoldState.drawerState.open() }
                }

            }) {
                Image(painter = painterResource(id = R.drawable.ic_home_menu), contentDescription = "title")
            }
        },
        title = { TitleText(Modifier.fillMaxWidth(), uiState.workspaceName) },
        actions = {
            Image(
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .clickable(onClick = addMeeting),
                painter = painterResource(id = R.drawable.ic_plus),
                contentDescription = null
            )
        },
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


@Preview
@Composable
private fun PagerItemPrev() {
    MeeronTheme {
        PagerItem(
            Meeting(
                title = "회의제목"
            )
        )
    }
}
