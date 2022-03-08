package fourtune.meeron.presentation.ui.create.date

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import forutune.meeron.domain.model.Date
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.CenterTextTopAppBar
import fourtune.meeron.presentation.ui.common.MeeronSingleButtonBackGround
import fourtune.meeron.presentation.ui.create.CreateTitle
import fourtune.meeron.presentation.ui.theme.MeeronTheme


@Composable
fun CreateMeetingDateScreen(
    viewModel: CreateMeetingDateViewModel = hiltViewModel(),
    onAction: () -> Unit = {},
    onNext: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState().collectAsState()
    CreateMeetingDateScreen(
        uiState = uiState
    ) { event ->
        when (event) {
            is CreateMeetingDateViewModel.Event.ChangeDate -> viewModel.changeDate(event.date)
            CreateMeetingDateViewModel.Event.OnBack -> onAction()
            CreateMeetingDateViewModel.Event.OnNext -> onNext("${uiState.date.year}년 ${uiState.date.month}월 ${uiState.date.hourOfDay}일")
        }
    }
}

@Composable
private fun CreateMeetingDateScreen(uiState: MeetingDateUiState, event: (CreateMeetingDateViewModel.Event) -> Unit = {}) {
    Scaffold(
        topBar = {
            CenterTextTopAppBar(
                onAction = { event(CreateMeetingDateViewModel.Event.OnBack) },
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
        MeeronSingleButtonBackGround(
            modifier = Modifier.padding(vertical = 40.dp, horizontal = 20.dp),
            text = stringResource(id = R.string.next),
            onClick = { event(CreateMeetingDateViewModel.Event.OnNext) }
        ) {
            DateScreen(date = uiState.date) { context, date ->
                showDatePickerDialog(context, date, event)
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
        CreateTitle(title = R.string.create_data_title)
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

private fun showDatePickerDialog(context: Context, date: Date, event: (CreateMeetingDateViewModel.Event) -> Unit) {
    DatePickerDialog(
        context,
        R.style.DatePickerStyle,
        { _, year, month, dayOfMonth ->
            event(CreateMeetingDateViewModel.Event.ChangeDate(Date(year, month + 1, dayOfMonth)))
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