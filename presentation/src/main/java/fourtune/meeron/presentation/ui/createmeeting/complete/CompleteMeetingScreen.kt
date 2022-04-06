package fourtune.meeron.presentation.ui.createmeeting.complete

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import forutune.meeron.domain.model.Meeting
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.MeeronButtonBackGround
import fourtune.meeron.presentation.ui.common.MeeronProgressIndicator
import fourtune.meeron.presentation.ui.common.text.TextGuideLineItem
import fourtune.meeron.presentation.ui.common.topbar.CenterTextTopAppBar
import fourtune.meeron.presentation.ui.createmeeting.CreateTitle
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CompleteMeetingScreen(
    viewModel: CompleteMeetingViewModel = hiltViewModel(),
    onPrevious: () -> Unit = {},
    onNext: () -> Unit = {},
    onAction: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.toast.collectLatest {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }
    val showLoading by viewModel.showLoading.collectAsState()

    Scaffold(topBar = {
        CenterTextTopAppBar(
            onAction = onAction,
            text = {
                Text(
                    text = stringResource(id = R.string.create_meeting),
                    fontSize = 18.sp,
                    color = colorResource(id = R.color.black)
                )
            })
    }) {
        MeeronProgressIndicator(showLoading)
        MeeronButtonBackGround(
            rightClick = {
                viewModel.createMeeting(onNext)
            },
            leftClick = onPrevious,
            rightText = "바로가기",
        ) {
            Content(
                meeting = uiState.meeting,
                owners = uiState.owners,
                agendaSize = uiState.meeting.agenda.size,
                participants = uiState.meeting.participants.size
            )
        }
    }
}

@Composable
private fun Content(meeting: Meeting, owners: String, agendaSize: Int, participants: Int) {
    Column {
        CreateTitle(title = R.string.complete_create)
        Text(text = meeting.title, fontSize = 18.sp, color = colorResource(id = R.color.dark_gray))
        Spacer(modifier = Modifier.padding(8.dp))
        TextGuideLineItem("회의 날짜", meeting.date.displayString())
        TextGuideLineItem("회의 성격", meeting.purpose)
        if (meeting.ownerIds.isNotEmpty()) TextGuideLineItem(stringResource(id = R.string.owners), owners)
        TextGuideLineItem("담당 팀", meeting.team.name)
        TextGuideLineItem("아젠다", String.format("%d개", agendaSize))
        TextGuideLineItem("참가자", String.format("%d명", participants))
    }
}

@Preview
@Composable
private fun Preview() {
    CompleteMeetingScreen()
}