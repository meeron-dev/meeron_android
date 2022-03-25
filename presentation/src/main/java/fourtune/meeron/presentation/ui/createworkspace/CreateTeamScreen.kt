package fourtune.meeron.presentation.ui.createworkspace

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.CenterTextTopAppBar
import fourtune.meeron.presentation.ui.common.MeeronButtonBackGround
import fourtune.meeron.presentation.ui.common.MeeronProgressIndicator
import fourtune.meeron.presentation.ui.common.action.ContentFactory
import fourtune.meeron.presentation.ui.common.action.MeeronActionBox

@Composable
fun CreateTeamScreen(
    viewModel: CreateTeamViewModel = hiltViewModel(),
    onPrevious: () -> Unit = {},
    onNext: (workspaceId: Long) -> Unit = {}
) {
    val showLoading by viewModel.showLoading.collectAsState()
    var teamName by remember {
        mutableStateOf("")
    }

    Scaffold(
        topBar = { CenterTextTopAppBar(text = "워크 스페이스 생성") },
        content = {
            MeeronButtonBackGround(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 50.dp),
                leftClick = onPrevious,
                rightEnable = teamName.isNotEmpty(),
                rightClick = {
                    viewModel.createWorkSpace(teamName, onNext)
                }
            ) {
                MeeronProgressIndicator(showLoading)
                Column {
                    Text(text = "첫번째 팀을\n생성해보세요.", fontSize = 25.sp, color = colorResource(id = R.color.black))
                    Spacer(modifier = Modifier.padding(5.dp))
                    Text(text = "언제든지 추가하고 편집할 수 있어요", fontSize = 15.sp, color = colorResource(id = R.color.gray))
                    Spacer(modifier = Modifier.padding(55.dp))
                    MeeronActionBox(
                        factory = ContentFactory.LimitTextField(
                            text = teamName,
                            onValueChange = { teamName = it },
                            isEssential = false,
                            limit = 10
                        ), title = ""
                    )
                }

            }
        }
    )
}

@Preview
@Composable
private fun Preview() {
    CreateTeamScreen()
}