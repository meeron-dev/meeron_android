package fourtune.meeron.presentation.ui.calendar

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
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.calendar.decorator.EventDecorator
import fourtune.meeron.presentation.ui.calendar.decorator.SelectionDecorator
import fourtune.meeron.presentation.ui.common.CircleBackgroundText
import fourtune.meeron.presentation.ui.theme.MeeronTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CalendarScreen(viewModel: CalendarViewModel = hiltViewModel(), onBack: () -> Unit = {}, showAll: () -> Unit = {}) {
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
                        text = "나의 캘린더",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.black)
                    )
                }
            )
        }
    ) {
        val currentDay by viewModel.currentDay().collectAsState()
        val scope = rememberCoroutineScope()
        Column(
            modifier = Modifier
                .background(colorResource(id = R.color.background))
                .padding(top = 36.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                CalendarTitle(
                    currentDay = currentDay,
                    previousEvent = { viewModel.goToPrevious() },
                    nextEvent = { viewModel.goToNext() }
                )
            }
            Text(
                modifier = Modifier
                    .clickable(onClick = showAll)
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
            Calendar(scope, viewModel)
            Divider(color = colorResource(id = R.color.white))
        }
    }
}

@Composable
private fun Calendar(
    scope: CoroutineScope,
    viewModel: CalendarViewModel
) {
    AndroidView(factory = { context ->
        MaterialCalendarView(context).apply {
            scope.launch {
                viewModel.event().collectLatest {
                    when (it) {
                        Event.Next -> goToNext()
                        Event.Previous -> goToPrevious()
                    }
                }
            }
            val selectionDecor by lazy { SelectionDecorator(context) }
            topbarVisible = false
            setDateTextAppearance(R.style.CalendarTextAppearance)
            setWeekDayTextAppearance(R.style.CalendarWeekAppearance)
            setBackgroundColor(ContextCompat.getColor(context, R.color.background))
            addDecorators(
                EventDecorator(
                    context,
                    CalendarDay.from(
                        viewModel.currentDay().value.year,
                        viewModel.currentDay().value.month,
                        viewModel.currentDay().value.day
                    )
                ),
                selectionDecor
            )
            setOnDateChangedListener { widget, date, selected ->
                selectionDecor.setDate(date)
                widget.invalidateDecorators()
            }
            setOnMonthChangedListener { _, date ->
                viewModel.changeDay(date)
            }

        }
    })
}

@Composable
private fun CalendarTitle(
    currentDay: CalendarDay,
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
            text = "${currentDay.year}",
            fontSize = 16.sp,
            color = colorResource(id = R.color.dark_gray)
        )
        Text(
            text = String.format(stringResource(id = R.string.calendar_month), currentDay.month),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.black)
        )
    }
    IconButton(onClick = nextEvent) {
        Image(painter = painterResource(id = R.drawable.ic_right_arrow), contentDescription = null)
    }
}

@Composable
fun CalendarDetail(index: Int = 0, isMyCalendar: Boolean = false) {
    Row {
        CircleBackgroundText(
            modifier = Modifier
                .background(colorResource(id = R.color.primary))
                .size(24.dp),
            "${index + 1}",
            colorResource(id = R.color.white)
        )
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = "title 입니다.",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.dark_primary)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "AM 10:00~ AM 11:30", fontSize = 12.sp, color = colorResource(id = R.color.medium_primary))
                if (isMyCalendar) {
                    Text(
                        text = "Team name",
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.medium_primary),
                    )
                } else {
                    Image(painter = painterResource(id = R.drawable.ic_right_arrow), contentDescription = null)
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
            CalendarDetail(isMyCalendar = true)
            CalendarDetail(isMyCalendar = false)
        }
    }
}

@Preview
@Composable
private fun CalendarPrev() {
    MeeronTheme {
        CalendarScreen()
    }
}