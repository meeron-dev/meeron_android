package fourtune.meeron.presentation.ui.team

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import forutune.meeron.domain.model.WorkspaceUser
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.CenterTextTopAppBar
import fourtune.meeron.presentation.ui.common.DeletedUserItem

@Composable
fun AdministerTeamScreen(
    viewModel: AdministerTeamViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    goToAddTeamMember: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    var openDeleteDialog by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            CenterTextTopAppBar(
                onNavigation = onBack,
                onAction = null,
                text = {
                    Text(
                        text = "팀 관리하기",
                        fontSize = 18.sp,
                        color = colorResource(id = R.color.black),
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        content = {
            AdministerTeamScreen(
                uiState = uiState,
                goToAddTeamMember = goToAddTeamMember,
                deleteTeamMember = viewModel::deletedTeamMember,
                deleteTeam = { openDeleteDialog = true }
            )
        }
    )

    if (openDeleteDialog) {
        DeleteDialog({ openDeleteDialog = false }, viewModel::deleteTeam)
    }
}

@Composable
private fun DeleteDialog(
    onDismissRequest: () -> Unit = {},
    onClickDelete: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        buttons = {
            Row {
                Button(onClick = onDismissRequest) {
                    Text(text = "닫기")
                }
                Button(onClick = onClickDelete) {
                    Text(text = "삭제하기")
                }
            }
        },
        title = {
            Text(text = "해당 팀을 정말 삭제하시겠습니까?", fontSize = 16.sp, color = colorResource(id = R.color.dark_gray))
        }
    )
}

@Composable
private fun AdministerTeamScreen(
    uiState: AdministerTeamViewModel.UiState,
    goToAddTeamMember: () -> Unit = {},
    deleteTeamMember: (WorkspaceUser) -> Unit = {},
    deleteTeam: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Spacer(modifier = Modifier.padding(19.dp))
            TeamLabel(uiState.selectedTeam.name)
            Spacer(modifier = Modifier.padding(24.dp))
            TeamMembers(goToAddTeamMember, deleteTeamMember, uiState.teamMembers)
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, end = 18.dp, bottom = 50.dp), onClick = deleteTeam,
            contentPadding = PaddingValues(vertical = 18.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.dark_gray_white))
        ) {
            Text(text = "팀 삭제하기", fontSize = 18.sp, color = colorResource(id = R.color.dark_gray))
        }
    }
}

@Composable
private fun TeamMembers(
    goToAddTeamMember: () -> Unit,
    deleteTeamMember: (WorkspaceUser) -> Unit,
    teamMember: List<WorkspaceUser>
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "팀원", fontSize = 18.sp, color = colorResource(id = R.color.dark_gray))
        IconButton(onClick = goToAddTeamMember) {
            Image(painter = painterResource(id = R.drawable.ic_plus), contentDescription = null)
        }
    }
    Spacer(modifier = Modifier.padding(5.dp))
    LazyVerticalGrid(columns = GridCells.Fixed(4)) {
        items(teamMember) { user ->
            DeletedUserItem(user = user, onClickDelete = { deleteTeamMember(user) })
        }
    }
}

@Composable
private fun TeamLabel(teamName: String) {
    Text(
        text = "팀 이름",
        fontSize = 18.sp,
        color = colorResource(id = R.color.dark_gray),
    )
    Spacer(modifier = Modifier.padding(12.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = colorResource(id = R.color.light_gray_white),
                shape = RoundedCornerShape(5.dp)
            )
            .padding(horizontal = 18.dp, vertical = 6.dp)
    ) {
        Text(
            text = teamName,
            fontSize = 15.sp,
            color = colorResource(id = R.color.black)
        )
    }
}

@Preview
@Composable
private fun Preview() {
    AdministerTeamScreen(
        AdministerTeamViewModel.UiState(
            teamMembers = listOf(
                WorkspaceUser(nickname = "zero"),
                WorkspaceUser(nickname = "zero"),
                WorkspaceUser(nickname = "zero"),
                WorkspaceUser(nickname = "zero"),
            )
        )
    )
}