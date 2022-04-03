package fourtune.meeron.presentation.ui.detail

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Composable
fun MeetingDetailScreen(viewModel: MeetingDetailViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            MeetingDetailTopBar(
                meeting = uiState.meeting,
                ownerNames = uiState.ownerNames
            )
        },
        content = {
            MeetingDetailContent(uiState = uiState)
        }
    )

}

@Composable
fun MeetingDetailTopBar(meeting: Meeting, ownerNames: String = "", onBack: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(id = R.color.light_gray))
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(modifier = Modifier.align(Alignment.CenterStart), onClick = onBack) {
                Image(painter = painterResource(id = R.drawable.ic_back), contentDescription = null)
            }
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "회의",
                fontSize = 18.sp,
                color = colorResource(id = R.color.black)
            )
        }
        Spacer(modifier = Modifier.padding(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "workspaceName", fontSize = 15.sp, color = colorResource(id = R.color.dark_gray))
            Text(text = "회의 플랜", fontSize = 12.sp, color = colorResource(id = R.color.gray))
        }
        Spacer(modifier = Modifier.padding(7.dp))
        Text(text = "회의 제목", fontSize = 17.sp, color = colorResource(id = R.color.black))
        Spacer(modifier = Modifier.padding(31.dp))
        TopbarContent("주관", meeting.team.name, meeting.date.displayString())
        Spacer(modifier = Modifier.padding(2.dp))
        TopbarContent("관리자", ownerNames, meeting.time)
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
fun MeetingDetailContent(uiState: MeetingDetailViewModel.UiState) {
    Column {
        uiState.meeting.agenda.forEach { agenda ->
            DetailItem(title = "아젠다", onClickDetail = { /*TODO*/ }, {
                Row {
                    Image(painter = painterResource(id = R.drawable.ic_meeting_clip), contentDescription = null)
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "${agenda.fileInfos.size}",
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.dark_gray)
                    )
                }
            })
        }
        Divider(color = colorResource(id = R.color.light_gray), thickness = 3.dp)

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(vertical = 20.dp, horizontal = 18.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "참가자", fontSize = 20.sp, color = colorResource(id = R.color.black))
                    IconButton(onClick = { }) {
                        Image(painter = painterResource(id = R.drawable.ic_edit), contentDescription = null)
                    }
                }
                Spacer(modifier = Modifier.padding(1.dp))
                Text(
                    text = "${uiState.meeting.participants.size}명 예정",
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.dark_gray)
                )
            }
            uiState.teamStates.forEach { teamState ->
                DetailItem(title = teamState.teamName) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "${teamState.attends + teamState.absents + teamState.unknowns}명 예정")
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            StateItem(R.drawable.ic_circle, "${teamState.attends}")
                            StateItem(R.drawable.ic_x, "${teamState.absents}")
                            StateItem(R.drawable.ic_qeustion_mark, "${teamState.unknowns}")
                        }
                    }
                }
            }

        }


    }
}

@Composable
fun DetailItem(
    title: String,
    onClickDetail: () -> Unit = {},
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
            Text(text = title, fontSize = 17.sp, color = colorResource(id = R.color.black), lineHeight = 39.sp)
            IconButton(onClick = onClickDetail) {
                Image(painter = painterResource(id = R.drawable.ic_right_arrow), contentDescription = null)
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
        Meeting(
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

@Composable
private fun StateItem(@DrawableRes resource: Int, count: String) {
    Image(painter = painterResource(id = resource), contentDescription = null)
    Spacer(modifier = Modifier.padding(2.dp))
    Text(text = count, fontSize = 13.sp, color = colorResource(id = R.color.dark_gray))
}