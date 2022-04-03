package fourtune.meeron.presentation.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import forutune.meeron.domain.model.Meeting
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.CenterTextTopAppBar

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
            MeetingDetailContent()
        }
    )

}

@Composable
fun MeetingDetailTopBar(meeting: Meeting = Meeting(), ownerNames: String = "", onBack: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(id = R.color.light_gray))
    ) {
        CenterTextTopAppBar(
            onNavigation = onBack,
            text = { Text(text = "회의", fontSize = 18.sp, color = colorResource(id = R.color.black)) }
        )
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
fun MeetingDetailContent() {

}

@Preview
@Composable
private fun Preview1() {
    MeetingDetailTopBar()
}

@Preview
@Composable
private fun Preview2() {
    MeetingDetailContent()
}