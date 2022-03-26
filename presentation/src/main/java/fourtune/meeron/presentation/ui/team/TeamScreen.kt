package fourtune.meeron.presentation.ui.team

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import forutune.meeron.domain.model.Team
import fourtune.meeron.presentation.R

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

@Composable
fun TeamScreen(viewModel: TeamViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = colorResource(id = R.color.light_gray))
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
                    itemsIndexed(uiState.teams) { index, team ->
                        TeamCircleItem(teamItem = TeamItems.values()[index], team = team)
                    }
                    item {
                        TeamCircleItem(teamItem = TeamItems.None, team = null)
                    }
                }
            }

        },
        content = {

        }
    )
}

@Composable
fun TeamCircleItem(teamItem: TeamItems = TeamItems.None, team: Team?) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .size(56.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(id = teamItem.drawable),
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 16.dp),
            text = team?.name?.substring(0..1) ?: "NONE",
            fontSize = if (team == null) 12.sp else 15.sp,
            color = colorResource(id = if (team == null) R.color.gray else R.color.dark_gray_white),
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
private fun Preview() {
    TeamScreen()
}

@Preview
@Composable
private fun Preview2() {
    TeamCircleItem(team = Team(name = "개발 팀"))
}