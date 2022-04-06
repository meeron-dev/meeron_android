package fourtune.meeron.presentation.ui.home.team.createcomplete

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.MeeronSingleButtonBackGround
import fourtune.meeron.presentation.ui.common.topbar.CenterTextTopAppBar

@Composable
fun TeamCreateCompleteScreen(viewModel: TeamCreateCompleteViewModel = hiltViewModel(), onAction: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterTextTopAppBar(
                onAction = onAction,
                text = {
                    Text(
                        text = "팀 생성하기",
                        fontSize = 18.sp,
                        color = colorResource(id = R.color.black),
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        content = {
            TeamCreateCompleteScreen(uiState, onAction)
        }
    )

}

@Composable
private fun TeamCreateCompleteScreen(
    uiState: TeamCreateCompleteViewModel.UiState,
    onAction: () -> Unit = {}
) {
    MeeronSingleButtonBackGround(
        text = "확인",
        onClick = onAction,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "팀이\n생성되었습니다.",
                fontSize = 25.sp,
                color = colorResource(id = R.color.black),
                lineHeight = 39.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.padding(15.dp))
            Text(
                text = uiState.teamName,
                fontSize = 18.sp,
                color = colorResource(id = R.color.dark_gray),
                lineHeight = 37.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.padding(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "팀원",
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.dark_gray),
                    lineHeight = 35.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.padding(44.dp))
                Text(
                    text = "${uiState.teamMemberCount}명",
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.dark_gray),
                    lineHeight = 35.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    TeamCreateCompleteScreen(uiState = TeamCreateCompleteViewModel.UiState("팀 이름입니다.", 123))
}