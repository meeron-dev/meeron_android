package fourtune.meeron.presentation.ui.create.complete

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import forutune.meeron.domain.model.Meeting
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.CenterTextTopAppBar
import fourtune.meeron.presentation.ui.common.MeeronButtonBackGround
import fourtune.meeron.presentation.ui.create.CreateTitle

@Composable
fun CompleteMeetingScreen(
    viewModel: CompleteMeetingViewModel = hiltViewModel(),
    onPrevious: () -> Unit = {},
    onNext: () -> Unit = {},
    onAction: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
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
        MeeronButtonBackGround(
            modifier = Modifier.padding(vertical = 40.dp, horizontal = 20.dp),
            rightClick = onNext,
            leftClick = onPrevious,
            rightText = "바로가기"
        ) {
            Content(uiState.meeting, uiState.meeting.agenda.size, uiState.meeting.participants.size)
        }
    }
}

@Composable
private fun Content(meeting: Meeting, agendaSize: Int, participants: Int) {
    Column {
        CreateTitle(title = R.string.complete_create)
        Text(text = meeting.title, fontSize = 18.sp, color = colorResource(id = R.color.dark_gray))
        Spacer(modifier = Modifier.padding(8.dp))
        MeetingItem("회의 날짜", meeting.date)
        MeetingItem("회의 성격", meeting.purpose)
        MeetingItem("공동 관리자", meeting.owner)
        MeetingItem("담당 팀", meeting.team.name)
        MeetingItem("아젠다", String.format("%d개", agendaSize))
        MeetingItem("참가자", String.format("%d명", participants))
    }
}

@Composable
private fun MeetingItem(title: String, text: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (left, right) = createRefs()
            val guideLine = createGuidelineFromStart(0.3f)
            Text(
                modifier = Modifier.constrainAs(left) {
                    start.linkTo(parent.start)
                },
                text = title,
                fontSize = 14.sp,
                color = colorResource(id = R.color.dark_gray)
            )

            Text(
                modifier = Modifier.constrainAs(right) {
                    start.linkTo(guideLine)
                },
                text = text,
                fontSize = 14.sp,
                color = colorResource(id = R.color.dark_gray)
            )

        }
    }
}

@Preview
@Composable
private fun Preview() {
    CompleteMeetingScreen()
}