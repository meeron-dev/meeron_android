package fourtune.meeron.presentation.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import fourtune.meeron.presentation.ui.common.ProfileImage
import fourtune.meeron.presentation.ui.common.text.TextGuideLineItem
import fourtune.meeron.presentation.ui.common.topbar.CenterTextTopAppBar

@Composable
fun WorkspaceUserDetailScreen(
    viewModel: WorkspaceUserDetailViewModel = hiltViewModel(),
    onAction: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    WorkspaceUserDetailScreen(uiState = uiState, onAction)
}

@Composable
private fun WorkspaceUserDetailScreen(
    uiState: WorkspaceUserDetailViewModel.UiState,
    onAction: () -> Unit
) {
    Scaffold(
        topBar = {
            WorkspaceUserDetailTopBar(onAction)
        },
        content = {
            WorkspaceUserDetailContent(uiState)
        }
    )
}

@Composable
private fun WorkspaceUserDetailContent(uiState: WorkspaceUserDetailViewModel.UiState) {
    Column(modifier = Modifier.padding(vertical = 30.dp)) {
        ProfileImage(image = uiState.image)
        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            text = uiState.userName,
            fontSize = 20.sp,
            color = colorResource(id = R.color.black),
            fontWeight = FontWeight.Medium
        )
        Column(
            modifier = Modifier
                .background(color = colorResource(id = R.color.topbar_color))
                .padding(20.dp)
                .fillMaxSize()
        ) {
            TextGuideLineItem(title = "별명", text = uiState.workspaceUser.nickname)
            TextGuideLineItem(title = "직책", text = uiState.workspaceUser.position)
            TextGuideLineItem(title = "연락처", text = uiState.workspaceUser.phone)
            TextGuideLineItem(title = "소속팀", text = uiState.team.name)
            TextGuideLineItem(title = "이메일", text = uiState.workspaceUser.email)
        }
    }
}

@Composable
private fun WorkspaceUserDetailTopBar(onAction: () -> Unit) {
    CenterTextTopAppBar(
        text = {
            Text(
                text = "상태 작성하기",
                fontSize = 18.sp,
                color = colorResource(id = R.color.black),
                fontWeight = FontWeight.Bold
            )
        },
        onAction = onAction
    )
}

@Preview
@Composable
private fun Preview() {
    WorkspaceUserDetailScreen()
}