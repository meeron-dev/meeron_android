package fourtune.meeron.presentation.ui.detail

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.hilt.navigation.compose.hiltViewModel
import forutune.meeron.domain.model.MeetingState
import forutune.meeron.domain.model.Team
import forutune.meeron.domain.model.WorkspaceUser
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.UserItem
import fourtune.meeron.presentation.ui.common.topbar.CenterTextTopAppBar

@Composable
fun TeamDetailScreen(
    viewModel: TeamDetailViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    onClickWorkspaceUser: (WorkspaceUser, Team) -> Unit = { _, _ -> }
) {
    val uiState by viewModel.uiState.collectAsState()
    TeamDetailScreen(uiState, onBack) { onClickWorkspaceUser(it, uiState.team) }
}

@Composable
private fun TeamDetailScreen(
    uiState: TeamDetailViewModel.UiState,
    onBack: () -> Unit = {},
    onClickWorkspaceUser: (WorkspaceUser) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TeamDetailTopBar(onBack)
        },
        content = {
            TeamDetailContent(uiState, onClickWorkspaceUser)
        }
    )
}

@Composable
private fun TeamDetailTopBar(onBack: () -> Unit) {
    CenterTextTopAppBar(
        text = {
            Text(
                text = "참가자",
                fontSize = 18.sp,
                color = colorResource(id = R.color.black),
                fontWeight = FontWeight.Bold
            )
        },
        onNavigation = onBack
    )
}

@Composable
private fun TeamDetailContent(
    uiState: TeamDetailViewModel.UiState,
    onClickWorkspaceUser: (WorkspaceUser) -> Unit = {}
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Spacer(modifier = Modifier.padding(12.dp))
        Text(
            text = uiState.team.name,
            fontSize = 22.sp,
            color = colorResource(id = R.color.black),
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = "${uiState.members.values.sumOf { it.size }}명 예정",
            fontSize = 20.sp,
            color = colorResource(id = R.color.dark_gray)
        )

        Column {
            StateItem(
                "참여",
                R.drawable.ic_circle,
                uiState.members[MeetingState.Attend].orEmpty(),
                onClickWorkspaceUser
            )
            StateItem(
                "불참",
                R.drawable.ic_x,
                uiState.members[MeetingState.Absent].orEmpty(),
                onClickWorkspaceUser
            )
            StateItem(
                "미작성",
                R.drawable.ic_qeustion_mark,
                uiState.members[MeetingState.Unknown].orEmpty(),
                onClickWorkspaceUser
            )
        }

    }
}

@Composable
private fun StateItem(
    title: String,
    @DrawableRes drawable: Int,
    workspaceUsers: List<WorkspaceUser> = emptyList(),
    onClickWorkspaceUser: (WorkspaceUser) -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.padding(20.dp))
        StateTitle(title, "${workspaceUsers.size}", drawable)
        if (workspaceUsers.isNotEmpty()) {
            Spacer(modifier = Modifier.padding(12.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                items(
                    items = workspaceUsers,
                    key = { it.workspaceUserId }
                ) { user ->
                    UserItem(
                        modifier = Modifier.clickable { onClickWorkspaceUser(user) },
                        user = user,
                        selected = false,
                        admin = false
                    )
                }
            }
        } else {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 72.dp),
                text = "참여하는 팀원이 존재하지 않습니다.",
                fontSize = 15.sp,
                color = Color(0xffbdbdbd)
            )
        }
    }
}

@Composable
private fun StateTitle(title: String, count: String, @DrawableRes drawable: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(painter = painterResource(id = drawable), contentDescription = null)
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = title,
            fontSize = 17.sp,
            color = colorResource(id = R.color.dark_gray),
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.padding(12.dp))
        Text(
            text = count,
            fontSize = 17.sp,
            color = colorResource(id = R.color.light_gray),
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview
@Composable
private fun Preview() {
    TeamDetailScreen(
        uiState = TeamDetailViewModel.UiState(
            team = Team(name = "팀 이름입니다."),
        )
    )
}