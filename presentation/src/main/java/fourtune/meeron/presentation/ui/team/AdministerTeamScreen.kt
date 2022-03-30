package fourtune.meeron.presentation.ui.team

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.hilt.navigation.compose.hiltViewModel
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
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(modifier = Modifier.padding(19.dp))
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
                        text = uiState.selectedTeam.name,
                        fontSize = 15.sp,
                        color = colorResource(id = R.color.black)
                    )
                }
                Spacer(modifier = Modifier.padding(24.dp))
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
                    items(uiState.teamMembers) { user ->
                        DeletedUserItem(user = user, onClickDelete = { viewModel.deletedTeamMember(user) })
                    }
                }
            }
        }
    )
}

@Preview
@Composable
private fun Preview() {
    AdministerTeamScreen {

    }
}