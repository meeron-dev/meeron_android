package fourtune.meeron.presentation.ui.team.picker

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import forutune.meeron.domain.model.WorkspaceUser
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.CenterTextTopAppBar
import fourtune.meeron.presentation.ui.common.MeeronSingleButtonBackGround
import fourtune.meeron.presentation.ui.common.UserItem

@Composable
fun TeamMemberPickerScreen(
    viewModel: TeamMemberPickerViewModel = hiltViewModel(),
    onBack: () -> Unit,
    goToMain: () -> Unit,
    goToTeamCreateComplete: (teamId: Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val deleteTeam = remember {
        { viewModel.deleteTeamIfAdded() }
    }
    BackHandler { deleteTeam(); onBack() }

    Scaffold(
        topBar = {
            CenterTextTopAppBar(
                text = {
                    Text(
                        text = uiState.type.title,
                        fontSize = 18.sp,
                        color = colorResource(id = R.color.black),
                        fontWeight = FontWeight.Bold
                    )
                },
                onAction = {
                    deleteTeam();
                    if (uiState.type == TeamMemberPickerViewModel.Type.Add) {
                        goToMain()
                    } else {
                        onBack()
                    }
                },
                onNavigation = if (uiState.type == TeamMemberPickerViewModel.Type.Add) {
                    { deleteTeam(); onBack() }
                } else null,
            )
        },
        content = {
            TeamMemberPickerScreen(uiState) { selectedTeamMember ->
                viewModel.addTeamMember(selectedTeamMember) {
                    goToTeamCreateComplete(uiState.teamId)
                }
            }
        }
    )

}

@Composable
private fun TeamMemberPickerScreen(
    uiState: TeamMemberPickerViewModel.UiState,
    onSelectedTeamMember: (List<WorkspaceUser>) -> Unit = {}
) {
    val selectedTeamMember = remember {
        mutableStateListOf<WorkspaceUser>()
    }
    MeeronSingleButtonBackGround(
        onClick = { onSelectedTeamMember(selectedTeamMember) },
        enable = if (uiState.type == TeamMemberPickerViewModel.Type.Add) true else selectedTeamMember.isNotEmpty()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "추가할 팀원을\n선택해주세요.",
                fontSize = 25.sp,
                color = colorResource(id = R.color.black),
                lineHeight = 39.sp,
                fontWeight = FontWeight.Medium
            )
            if (uiState.type == TeamMemberPickerViewModel.Type.Add) {
                Spacer(modifier = Modifier.padding(6.dp))
                Text(text = uiState.workspaceName, fontSize = 15.sp, color = colorResource(id = R.color.gray))
                Text(
                    text = "팀은 최대 5개까지 생성이 가능합니다.",
                    fontSize = 13.sp,
                    color = colorResource(id = R.color.light_gray)
                )
                Spacer(modifier = Modifier.padding(21.dp))
            } else {
                Spacer(modifier = Modifier.padding(25.dp))
            }

            Text(
                text = "NONE",
                fontSize = 18.sp,
                color = colorResource(id = R.color.gray),
                lineHeight = 26.sp
            )
            Text(
                text = "${selectedTeamMember.size}명 선택됨",
                fontSize = 14.sp,
                color = colorResource(id = R.color.dark_primary),
                lineHeight = 37.sp
            )
            Spacer(modifier = Modifier.padding(5.dp))
            if (uiState.notJoinedTeamWorkspaceUser.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "선택할 수 있는 팀원이 없습니다.", fontSize = 14.sp, color = Color(0xffbdbdbd))
                }
            } else {
                LazyVerticalGrid(columns = GridCells.Fixed(4)) {
                    items(uiState.notJoinedTeamWorkspaceUser) { user ->
                        UserItem(
                            modifier = Modifier.clickable {
                                if (selectedTeamMember.contains(user)) {
                                    selectedTeamMember.remove(user)
                                } else {
                                    selectedTeamMember.add(user)
                                }
                            },
                            user = user,
                            selected = selectedTeamMember.contains(user),
                            admin = false
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    TeamMemberPickerScreen(uiState = TeamMemberPickerViewModel.UiState(TeamMemberPickerViewModel.Type.Add))
}

@Preview
@Composable
private fun Preview2() {
    TeamMemberPickerScreen(uiState = TeamMemberPickerViewModel.UiState(TeamMemberPickerViewModel.Type.Administer))
}