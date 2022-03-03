package fourtune.meeron.presentation.ui.create.time

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import forutune.meeron.domain.model.Time
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.CenterTextTopAppBar
import fourtune.meeron.presentation.ui.common.MerronButton
import fourtune.meeron.presentation.ui.create.CreateMeetingTimeViewModel
import fourtune.meeron.presentation.ui.create.MeetingTimeUiState
import fourtune.meeron.presentation.ui.theme.MeeronTheme

private sealed interface CreateMeetingTimeEvent {
    class ChangeTime(val key: Int, val hour: Int, val minute: Int) : CreateMeetingTimeEvent
    object Previous : CreateMeetingTimeEvent
    object Next : CreateMeetingTimeEvent
    object Exit : CreateMeetingTimeEvent
}

@Composable
fun CreateMeetingTimeScreen(
    timeViewModel: CreateMeetingTimeViewModel = hiltViewModel(),
    onAction: () -> Unit = {},
    onPrevious: () -> Unit = {}
) {
    val uiState by timeViewModel.uiState().collectAsState()
    CreateMeetingTimeScreen(
        uiState,
        event = { event ->
            when (event) {
                is CreateMeetingTimeEvent.ChangeTime -> timeViewModel.changeTime(
                    event.key,
                    event.hour,
                    event.minute
                )
                CreateMeetingTimeEvent.Exit -> onAction()
                CreateMeetingTimeEvent.Next -> TODO()
                CreateMeetingTimeEvent.Previous -> onPrevious()
            }
        }
    )
}

@Composable
private fun CreateMeetingTimeScreen(
    uiState: MeetingTimeUiState,
    event: (CreateMeetingTimeEvent) -> Unit
) {
    Scaffold(
        topBar = {
            CenterTextTopAppBar(
                text = {
                    Text(
                        text = stringResource(id = R.string.create_meeting),
                        fontSize = 18.sp,
                        color = colorResource(id = R.color.black)
                    )
                },
                onAction = { event(CreateMeetingTimeEvent.Exit) }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 50.dp, horizontal = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            TimeScreen(uiState, event)
            MerronButton(
                leftClick = { event(CreateMeetingTimeEvent.Previous) },
                rightClick = { event(CreateMeetingTimeEvent.Next) }
            )
        }


    }
}

private fun showTimePickerDialog(
    context: Context,
    onChangeTime: (hour: Int, minute: Int) -> Unit
) {
    TimePickerDialog(
        context,
        R.style.TimePickerStyle,
        { _, hourOfDay, minute ->
            onChangeTime(hourOfDay, minute)
        },
        0,
        0,
        false
    ).show()
}

@Composable
private fun TimeScreen(
    uiState: MeetingTimeUiState,
    createMeetingTimeEvent: (CreateMeetingTimeEvent) -> Unit
) {
    Column {
        Text(
            text = stringResource(id = R.string.create_time_title),
            fontSize = 25.sp,
            color = colorResource(id = R.color.black)
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text = uiState.currentDay,
            fontSize = 15.sp,
            color = colorResource(id = R.color.light_gray)
        )
        Spacer(modifier = Modifier.padding(21.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            uiState.timeMap.forEach {
                TimeField(text = it.key, time = it.value) { context: Context ->
                    showTimePickerDialog(
                        context = context,
                        onChangeTime = { hour: Int, minute: Int ->
                            createMeetingTimeEvent(
                                CreateMeetingTimeEvent.ChangeTime(it.key, hour, minute)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TimeField(
    text: Int,
    time: Time,
    openTimePicker: (context: Context) -> Unit
) {
    val context = LocalContext.current

    Column(
        Modifier
            .width(IntrinsicSize.Min)
            .clickable { openTimePicker(context) }) {
        Text(
            text = stringResource(id = text),
            fontSize = 16.sp,
            color = colorResource(id = R.color.dark_gray)
        )
        Spacer(modifier = Modifier.padding(14.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = time.time, fontSize = 38.sp, color = colorResource(id = R.color.dark_gray))
                Spacer(modifier = Modifier.padding(13.dp))
                Text(text = time.hourOfDay, fontSize = 24.sp, color = colorResource(id = R.color.gray))
            }
            Divider()
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MeeronTheme {
        CreateMeetingTimeScreen()
    }
}