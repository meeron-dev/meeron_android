package fourtune.meeron.presentation.ui.create.complete

import androidx.compose.foundation.layout.Column
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
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.CenterTextTopAppBar
import fourtune.meeron.presentation.ui.common.MeeronButtonBackGround
import fourtune.meeron.presentation.ui.create.CreateTitle

@Composable
fun CompleteMeetingScreen(viewModel: CompleteMeetingViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(topBar = {
        CenterTextTopAppBar(
            onAction = { /*TODO*/ },
            text = {
                Text(
                    text = stringResource(id = R.string.create_meeting),
                    fontSize = 18.sp,
                    color = colorResource(id = R.color.black)
                )
            })
    }) {
        MeeronButtonBackGround(modifier = Modifier.padding(vertical = 40.dp, horizontal = 20.dp)) {
            Column {
                Text(text = "ㅏ이틀", fontSize = 18.sp, color = colorResource(id = R.color.dark_gray))
                MeetingItem("회의 날짜", "2022년 2월 4일")
            }
        }
    }
}

@Composable
private fun MeetingItem(title: String, text: String) {
    Column {
        CreateTitle(title = R.string.complete_create)
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