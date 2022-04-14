package fourtune.meeron.presentation.ui.home.team

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import forutune.meeron.domain.model.Team
import forutune.meeron.domain.model.WorkspaceUser
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.UserItem

enum class TeamItems(
    @DrawableRes val drawable: Int
) {
    First(R.drawable.ic_team_1),
    Second(R.drawable.ic_team_2),
    Third(R.drawable.ic_team_3),
    Fourth(R.drawable.ic_team_4),
    Fifth(R.drawable.ic_team_5),
    None(R.drawable.ic_team_none),
}

sealed interface TeamEvent {
    class AdministerTeam(val teamState: TeamViewModel.TeamState.Normal) : TeamEvent
    object OpenCalendar : TeamEvent
    class GoToDetail(val workspaceUser: WorkspaceUser, val teamState: TeamViewModel.TeamState) : TeamEvent
}

@Composable
fun TeamScreen(
    viewModel: TeamViewModel = hiltViewModel(),
    teamEvent: (TeamEvent) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    TeamScreen(
        uiState = uiState,
        event = teamEvent
    )
}

@Composable
private fun TeamScreen(uiState: TeamViewModel.UiState, event: (TeamEvent) -> Unit = {}) {
    val selectedTeam = uiState.selectedTeam
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.padding(7.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 22.dp, horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = when (selectedTeam) {
                    is TeamViewModel.TeamState.Normal -> selectedTeam.team.name
                    is TeamViewModel.TeamState.None -> selectedTeam.team.name
                },
                fontSize = 21.sp,
                color = colorResource(id = R.color.black)
            )
            if (selectedTeam is TeamViewModel.TeamState.Normal) {
                Row {
                    IconButton(onClick = { event(TeamEvent.OpenCalendar) }) {
                        Image(painter = painterResource(id = R.drawable.ic_calendar), contentDescription = null)
                    }
                    if (uiState.isAdmin) {
                        Spacer(modifier = Modifier.padding(4.dp))
                        IconButton(onClick = { event(TeamEvent.AdministerTeam(selectedTeam)) }) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_setting),
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
        Column(modifier = Modifier.padding(top = 24.dp, start = 20.dp, end = 20.dp)) {
            if (uiState.selectedTeam is TeamViewModel.TeamState.None) {
                Text(text = "아직 팀이 없는 멤버들입니다.", fontSize = 15.sp, color = colorResource(id = R.color.light_gray))
            } else {
                Text(text = "팀원", fontSize = 17.sp, color = colorResource(id = R.color.dark_gray))
            }
            Spacer(modifier = Modifier.padding(5.dp))
            if (uiState.teamMembers.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "아직 팀원이 존재하지 않습니다.",
                        fontSize = 15.sp,
                        color = colorResource(id = R.color.gray)
                    )
                }
            } else {
                LazyVerticalGrid(columns = GridCells.Fixed(4)) {
                    items(uiState.teamMembers) { workspaceUser: WorkspaceUser ->
                        UserItem(
                            modifier = Modifier.clickable { event(TeamEvent.GoToDetail(workspaceUser, selectedTeam)) },
                            user = workspaceUser,
                            selected = false,
                            admin = workspaceUser.workspaceAdmin
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TeamTopBar(
    isAdmin: Boolean,
    teams: List<Team>,
    selectedTeam: TeamViewModel.TeamState,
    showNone: Boolean,
    onClickTeam: (team: Team) -> Unit = {},
    onClickNone: () -> Unit = {},
    onClickCreate: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(id = R.color.topbar_color))
            .padding(bottom = 18.dp)
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 23.dp),
            text = "팀",
            fontSize = 18.sp,
            color = colorResource(id = R.color.black),
            fontWeight = FontWeight.Bold
        )
        LazyRow(modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(horizontal = 20.dp)) {
            item {
                if (isAdmin) {
                    TeamCircleItem(
                        drawable = R.drawable.ic_team_plus,
                        teamName = "",
                        enable = teams.size < 5,
                        onClick = onClickCreate
                    )
                }
            }
            itemsIndexed(teams) { index, team ->
                TeamCircleItem(
                    drawable = TeamItems.values()[index].drawable,
                    teamName = team.name.substring(0..1),
                    isSelected = team == (selectedTeam as? TeamViewModel.TeamState.Normal)?.team ?: false,
                    fontSize = 15.sp,
                    fontColor = colorResource(id = R.color.dark_gray_white),
                ) {
                    onClickTeam(team)
                }
            }
            item {
                if (showNone) {
                    TeamCircleItem(
                        drawable = TeamItems.None.drawable,
                        teamName = "NONE",
                        fontSize = 12.sp,
                        isSelected = selectedTeam is TeamViewModel.TeamState.None,
                        fontColor = colorResource(id = if (selectedTeam is TeamViewModel.TeamState.None) R.color.primary else R.color.gray),
                        onClick = onClickNone
                    )
                }
            }
        }
    }
}

@Composable
fun TeamCircleItem(
    @DrawableRes drawable: Int = -1,
    teamName: String,
    isSelected: Boolean = false,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontColor: Color = Color.Unspecified,
    enable: Boolean = true,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .size(56.dp)
            .clickable(onClick = onClick, enabled = enable),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(id = drawable),
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 16.dp),
            text = teamName,
            fontSize = fontSize,
            color = fontColor,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Preview
@Composable
private fun Preview() {
    TeamScreen(TeamViewModel.UiState())
}

@Preview
@Composable
private fun Preview2() {
    TeamCircleItem(teamName = "개발 팀")
}

@Preview
@Composable
private fun Preview3() {
    TeamTopBar(
        isAdmin = true,
        teams = listOf(Team(id = 1, name = "team1"), Team(id = 2, name = "team2")),
        selectedTeam = TeamViewModel.TeamState.Normal(Team(name = "team1")),
        showNone = true
    )
}