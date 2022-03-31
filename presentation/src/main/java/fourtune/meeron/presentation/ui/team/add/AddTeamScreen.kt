package fourtune.meeron.presentation.ui.team.add

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.CenterTextTopAppBar
import fourtune.meeron.presentation.ui.common.MeeronSingleButtonBackGround
import fourtune.meeron.presentation.ui.common.action.ContentFactory
import fourtune.meeron.presentation.ui.common.action.MeeronActionBox

@Composable
fun AddTeamScreen(
    viewModel: AddTeamViewModel = hiltViewModel(),
    onAction: () -> Unit = {},
    openTeamPicker: (teamId: Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    AddTeamScreen(onAction, uiState) { teamName ->
        viewModel.addTeam(teamName, openTeamPicker)
    }

}

@Composable
private fun AddTeamScreen(
    onAction: () -> Unit,
    uiState: AddTeamViewModel.UiState,
    addTeam: (teamName: String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenterTextTopAppBar(text = {
                Text(
                    text = "팀 생성하기",
                    fontSize = 18.sp,
                    color = colorResource(id = R.color.black),
                    fontWeight = FontWeight.Bold
                )
            }, onAction = onAction)
        },
        content = {
            AddTeamScreen(addTeam, uiState)
        }
    )
}

@Composable
private fun AddTeamScreen(
    addTeam: (teamName: String) -> Unit,
    uiState: AddTeamViewModel.UiState
) {
    var teamName by remember {
        mutableStateOf("")
    }

    MeeronSingleButtonBackGround(
        text = stringResource(id = R.string.next),
        onClick = { addTeam(teamName) },
        enable = teamName.isNotEmpty()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "팀 이름을\n입력해주세요.",
                fontSize = 25.sp,
                color = colorResource(id = R.color.black),
                lineHeight = 39.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.padding(9.dp))
            Text(
                text = uiState.workspaceName,
                fontSize = 15.sp,
                color = colorResource(id = R.color.gray),
                lineHeight = 26.sp
            )
            Text(
                text = "팀은 최대 5개까지 생성이 가능합니다.",
                fontSize = 13.sp,
                color = colorResource(id = R.color.light_gray),
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.padding(50.dp))
            MeeronActionBox(
                factory = ContentFactory.LimitTextField(
                    text = teamName,
                    onValueChange = { teamName = it },
                    limit = 10,
                    isEssential = false
                ), title = ""
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AddTeamScreen(addTeam = {}, uiState = AddTeamViewModel.UiState())

}