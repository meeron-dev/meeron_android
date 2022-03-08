package fourtune.meeron.presentation.ui.common.bottomsheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.MeeronButtonBackGround

@Composable
fun TeamSelectScreen(
    teams: List<String> = emptyList(),
    onSelectTeam: (Int) -> Unit = {}
) {
    var selected: Int? by remember {
        mutableStateOf(null)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height((LocalConfiguration.current.screenHeightDp * 0.9).dp)
    ) {
        MeeronButtonBackGround(Modifier.padding(bottom = 50.dp)) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(top = 46.dp),
                    text = stringResource(R.string.team_select_title),
                    fontSize = 18.sp,
                    color = colorResource(id = R.color.dark_gray)
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 70.dp, start = 20.dp, end = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    itemsIndexed(teams) { index, item ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectTeam(index) }
                        ) {
                            Text(
                                text = item,
                                fontSize = 19.sp,
                                color = colorResource(id = if (index == selected) R.color.primary else R.color.dark_gray),
                                fontWeight = if (index == selected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }

}

@Preview
@Composable
private fun Preview() {
    TeamSelectScreen(listOf("hello", "world"))
}