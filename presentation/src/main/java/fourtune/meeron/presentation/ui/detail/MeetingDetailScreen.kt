package fourtune.meeron.presentation.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import forutune.meeron.domain.model.*
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.StateItem
import fourtune.meeron.presentation.ui.common.topbar.DetailTopBar

@Composable
fun MeetingDetailScreen(
    viewModel: MeetingDetailViewModel = hiltViewModel(),
    goToAgendaDetail: (Meeting) -> Unit,
    goToParticipantState: (Meeting) -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            MeetingDetailTopBar(
                workspaceName = uiState.workspaceInfo.workSpaceName,
                meeting = uiState.meeting,
                ownerNames = uiState.ownerNames,
                onBack = onBack
            )
        },
        content = {
            MeetingDetailContent(
                uiState = uiState,
                onClickAgenda = { goToAgendaDetail(uiState.meeting) },
                onClickParticipantState = { goToParticipantState(uiState.meeting) }
            )
        }
    )

}

@Composable
fun MeetingDetailTopBar(
    workspaceName: String = "",
    meeting: Meeting,
    ownerNames: String = "",
    onBack: () -> Unit = {}
) {
    DetailTopBar(title = "회의", onBack) {
        Spacer(modifier = Modifier.padding(5.dp))
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = workspaceName, fontSize = 15.sp, color = colorResource(id = R.color.dark_gray))
                Text(text = "회의 플랜", fontSize = 12.sp, color = colorResource(id = R.color.gray))
            }
            Spacer(modifier = Modifier.padding(7.dp))
            Text(
                text = meeting.title,
                fontSize = 17.sp,
                color = colorResource(id = R.color.black),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.padding(31.dp))
            TopbarContent("주관", meeting.team.name, meeting.date.displayString())
            Spacer(modifier = Modifier.padding(2.dp))
            TopbarContent("관리자", ownerNames, meeting.time)
            Spacer(modifier = Modifier.padding(8.dp))
        }
    }
}

@Composable
private fun TopbarContent(title: String, text: String, info: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (texts, infos) = createRefs()
            val guideLine = createGuidelineFromStart(0.2f)
            Text(
                text = title,
                fontSize = 14.sp,
                color = colorResource(id = R.color.dark_gray),
                fontWeight = FontWeight.Medium
            )

            Text(
                modifier = Modifier.constrainAs(texts) {
                    start.linkTo(guideLine)
                },
                text = text,
                fontSize = 14.sp,
                color = colorResource(id = R.color.dark_gray),
                fontWeight = FontWeight.Normal
            )
            Text(
                modifier = Modifier.constrainAs(infos) {
                    end.linkTo(parent.end)
                },
                text = info,
                fontSize = 12.sp,
                color = colorResource(id = R.color.gray)

            )
        }

    }
}

@Composable
fun MeetingDetailContent(
    uiState: MeetingDetailViewModel.UiState,
    onClickAgenda: () -> Unit = {},
    onClickParticipantState: () -> Unit = {}
) {
    Column {
        Agenda(onClickAgenda, uiState.meeting.agenda)
        Divider(color = colorResource(id = R.color.topbar_color), thickness = 3.dp)
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Participants(uiState.meeting.participants.size, onClickParticipantState)
            TeamStateLazyColumn(uiState.teamStates)
        }
    }
}

@Composable
private fun Agenda(
    onClickAgenda: () -> Unit,
    agendas: List<Agenda>
) {
    val enable = agendas.isNotEmpty()
    if (enable) {
        agendas.forEach { agenda ->
            AgendaDetailItem(onClickAgenda, agenda, enable)
        }
    } else {
        AgendaDetailItem(onClickAgenda = onClickAgenda, agenda = Agenda(), enable = enable)
    }
}

@Composable
private fun AgendaDetailItem(onClickAgenda: () -> Unit, agenda: Agenda, enable: Boolean) {
    DetailItem(title = "아젠다", enable = enable, onClickDetail = onClickAgenda) {
        Row {
            Image(
                painter = painterResource(id = if (enable) R.drawable.ic_meeting_clip else R.drawable.ic_meeting_clip_disable),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.padding(2.dp))
            Text(
                text = if (enable) "${agenda.fileInfos.size}" else "0",
                fontSize = 12.sp,
                color = colorResource(id = if (enable) R.color.dark_gray else R.color.gray)
            )
        }
    }
}

@Composable
private fun TeamStateLazyColumn(teamStates: List<TeamState>) {
    LazyColumn {
        itemsIndexed(
            items = teamStates,
            key = { _, item -> item.teamId }
        ) { index, teamState ->
            DetailItem(title = teamState.teamName) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${teamState.attends + teamState.absents + teamState.unknowns}명 예정",
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.gray)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        StateItem(R.drawable.ic_circle, "${teamState.attends}")
                        StateItem(R.drawable.ic_x, "${teamState.absents}")
                        StateItem(R.drawable.ic_qeustion_mark, "${teamState.unknowns}")
                    }
                }
            }
            if (index != teamStates.lastIndex) {
                Divider(color = Color(0xffe7e7e7))
            }
        }
    }
}

@Composable
private fun Participants(participantSize: Int, onClickParticipantState: () -> Unit = {}) {
    Column(modifier = Modifier.padding(vertical = 20.dp, horizontal = 18.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "참가자", fontSize = 20.sp, color = colorResource(id = R.color.black))
            IconButton(onClick = onClickParticipantState) {
                Image(painter = painterResource(id = R.drawable.ic_edit), contentDescription = null)
            }
        }
        Spacer(modifier = Modifier.padding(1.dp))
        Text(
            text = "${participantSize}명 예정",
            fontSize = 16.sp,
            color = colorResource(id = R.color.dark_gray)
        )
    }
}

@Composable
fun DetailItem(
    title: String,
    onClickDetail: () -> Unit = {},
    enable: Boolean = true,
    content: @Composable () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 18.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 17.sp,
                color = colorResource(id = if (enable) R.color.black else R.color.gray),
                lineHeight = 39.sp
            )
            IconButton(onClick = onClickDetail, enabled = enable) {
                Image(
                    painter = painterResource(id = if (enable) R.drawable.ic_right_arrow_21 else R.drawable.ic_right_arrow_disable_21),
                    contentDescription = null,
                )
            }
        }
        Spacer(modifier = Modifier.padding(2.dp))
        content()
    }
}

@Preview
@Composable
private fun Preview1() {
    MeetingDetailTopBar(
        workspaceName = "워크스페이스 이름입니다.",
        meeting = Meeting(
            title = "미팅 타이틀",
            date = Date(2022, 4, 4),
            time = "9:00AM~1:00PM",
            purpose = "목적은 이것입니다.",
            agenda = listOf(
                Agenda(1, "agenda1", issues = emptyList(), fileInfos = listOf(FileInfo("", "")))
            )
        )
    )
}

@Preview
@Composable
private fun Preview2() {
    MeetingDetailContent(
        MeetingDetailViewModel.UiState(
            meeting = Meeting(
                title = "미팅 타이틀",
                date = Date(2022, 4, 4),
                time = "9:00AM~1:00PM",
                purpose = "목적은 이것입니다.",
                agenda = listOf(
                    Agenda(1, "agenda1", issues = emptyList(), fileInfos = listOf(FileInfo("", "")))
                )
            ),
            ownerNames = "조재영, 류강윤",
            teamStates = listOf(
                TeamState(1, "팀1", 1, 2, 3),
                TeamState(2, "팀2", 2, 4, 0),
                TeamState(3, "팀3", 1, 1, 1),
            )
        )
    )
}

@Preview
@Composable
private fun Preview3() {
    DetailItem(title = "참가자") {

    }
}

