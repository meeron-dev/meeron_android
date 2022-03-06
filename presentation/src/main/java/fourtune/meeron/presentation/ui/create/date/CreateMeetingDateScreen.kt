package fourtune.meeron.presentation.ui.create.date

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import forutune.meeron.domain.model.Date
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.CenterTextTopAppBar
import fourtune.meeron.presentation.ui.theme.MeeronTheme

sealed interface CreateMeetingDateEvent {
    object OnBack : CreateMeetingDateEvent
    object OnNext : CreateMeetingDateEvent
    class ChangeDate(val date: Date) : CreateMeetingDateEvent
}

@Composable
fun CreateMeetingDateScreen(
    viewModel: CreateMeetingDateViewModel = hiltViewModel(),
    onAction: () -> Unit = {},
    onNext: () -> Unit = {}
) {
    val uiState by viewModel.uiState().collectAsState()
    CreateMeetingDateScreen(
        uiState = uiState
    ) { event ->
        when (event) {
            is CreateMeetingDateEvent.ChangeDate -> viewModel.changeDate(event.date)
            CreateMeetingDateEvent.OnBack -> onAction()
            CreateMeetingDateEvent.OnNext -> onNext()
        }
    }
}

@Composable
private fun CreateMeetingDateScreen(uiState: MeetingDateUiState, event: (CreateMeetingDateEvent) -> Unit = {}) {
    Scaffold(
        topBar = {
            CenterTextTopAppBar(
                onAction = { event(CreateMeetingDateEvent.OnBack) },
                text = {
                    Text(
                        text = stringResource(id = R.string.create_meeting),
                        fontSize = 18.sp,
                        color = colorResource(id = R.color.black)
                    )
                }
            )
        },
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 40.dp, horizontal = 20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            DateScreen(
                date = uiState.date
            ) { context, date ->
                showDatePickerDialog(context, date, event)
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { event(CreateMeetingDateEvent.OnNext) },
                contentPadding = PaddingValues(vertical = 18.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.primary))
            ) {
                Text(
                    text = stringResource(id = R.string.next),
                    fontSize = 18.sp,
                    color = colorResource(id = R.color.white),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun DateScreen(
    modifier: Modifier = Modifier,
    date: Date,
    openDatePicker: (context: Context, date: Date) -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.create_data_title),
            fontSize = 25.sp,
            color = colorResource(id = R.color.black)
        )
        Spacer(modifier = Modifier.padding(32.dp))
        Column(
            Modifier
                .clickable { openDatePicker(context, date) }
        ) {
            Text(
                text = date.toString(),
                fontSize = 25.sp,
                color = colorResource(id = R.color.black),
                maxLines = 1,
                overflow = TextOverflow.Visible
            )
            Divider(color = colorResource(id = R.color.light_gray))
        }
    }
}

private fun showDatePickerDialog(context: Context, date: Date, event: (CreateMeetingDateEvent) -> Unit) {
    DatePickerDialog(
        context,
        R.style.DatePickerStyle,
        { _, year, month, dayOfMonth ->
            event(CreateMeetingDateEvent.ChangeDate(Date(year, month + 1, dayOfMonth)))
        },
        date.year,
        date.month - 1,
        date.hourOfDay
    ).show()
}

@Preview
@Composable
private fun Preview() {
    MeeronTheme {
        CreateMeetingDateScreen()
    }
}