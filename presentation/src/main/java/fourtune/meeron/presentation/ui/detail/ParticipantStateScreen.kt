package fourtune.meeron.presentation.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.model.MeetingState
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.MeeronSingleButtonBackGround

@Composable
fun ParticipantStateScreen(viewModel: ParticipantStateViewModel = hiltViewModel(), onAction: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    ParticipantStateScreen(
        uiState = uiState,
        onClickButton = { viewModel.changeState(it, onAction) },
        onAction = onAction
    )
}

@Composable
private fun ParticipantStateScreen(
    uiState: ParticipantStateViewModel.UiState,
    onClickButton: (MeetingState) -> Unit = {},
    onAction: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            StateTopBar(onAction)
        },
        content = {
            StateContent(uiState, onClickButton)
        }
    )
}

@Composable
private fun StateTopBar(onAction: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 23.dp),
            text = "상태 작성하기",
            fontSize = 18.sp,
            color = colorResource(id = R.color.black),
            fontWeight = FontWeight.Bold
        )
        IconButton(modifier = Modifier.align(Alignment.CenterEnd), onClick = onAction) {
            Image(imageVector = Icons.Default.Close, contentDescription = null)
        }
    }
}

@Composable
private fun StateContent(uiState: ParticipantStateViewModel.UiState, onClickButton: (MeetingState) -> Unit = {}) {
    var select by remember {
        mutableStateOf(MeetingState.Unknown)
    }

    MeeronSingleButtonBackGround(onClick = { onClickButton(select) }, enable = select != MeetingState.Unknown) {
        Spacer(modifier = Modifier.padding(12.dp))
        Column(modifier = Modifier.padding()) {
            Text(text = uiState.workspaceName, fontSize = 15.sp, color = colorResource(id = R.color.dark_gray))
            Spacer(modifier = Modifier.padding(7.dp))
            Text(text = uiState.meeting.meetingName, fontSize = 17.sp, color = colorResource(id = R.color.dark_gray))
            Spacer(modifier = Modifier.padding(25.dp))
            Text(
                text = "회의 참여 여부",
                fontSize = 19.sp,
                color = colorResource(id = R.color.black),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.padding(50.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(onClick = { select = MeetingState.Attend }) {
                    Image(
                        painter = painterResource(id = if (select == MeetingState.Attend) R.drawable.ic_condition_circle_able else R.drawable.ic_condition_circle_disable),
                        contentDescription = null
                    )
                }
                IconButton(onClick = { select = MeetingState.Absent }) {
                    Image(
                        painter = painterResource(id = if (select == MeetingState.Absent) R.drawable.ic_condition_x_able else R.drawable.ic_condition_x_disable),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ParticipantStateScreen(
        ParticipantStateViewModel.UiState(
            Meeting(meetingName = "회의 제목", purpose = "목적은 존나깁니다~~~"),
            "워크스페이스 이름"
        )
    )
}