package fourtune.meeron.presentation.ui.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.team.TeamScreen
import fourtune.meeron.presentation.ui.team.TeamTopBar
import fourtune.meeron.presentation.ui.team.TeamViewModel
import fourtune.meeron.presentation.ui.theme.MeeronTheme

sealed class BottomNavi(@DrawableRes val image: Int, @StringRes val text: Int) {
    object Home : BottomNavi(R.drawable.ic_navi_home, R.string.home)
    object Team : BottomNavi(R.drawable.ic_navi_team, R.string.team)
    object My : BottomNavi(R.drawable.ic_navi_door, R.string.my_merron)
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    teamViewModel: TeamViewModel = hiltViewModel(),
    openCalendar: () -> Unit = {},
    addMeeting: () -> Unit = {},
    createWorkspace: () -> Unit = {}
) {

    val homeUiState by homeViewModel.uiState.collectAsState()
    val teamUiState by teamViewModel.uiState.collectAsState()

    var content: BottomNavi by remember {
        mutableStateOf(BottomNavi.Home)
    }

    val bottomBarSize = 90.dp
    val scaffoldState = rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            when (content) {
                BottomNavi.Home -> HomeTopBar(scaffoldState, homeUiState, addMeeting)
                BottomNavi.My -> {}
                BottomNavi.Team -> TeamTopBar(teams = teamUiState.teams)
            }

        },
        bottomBar = {
            BottomNavigation(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                    .height(bottomBarSize),
                onClick = { bottomNavi ->
                    content = bottomNavi
                }
            )
        },
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(300.dp)
            ) {
                Text(
                    text = "워크스페이스 추가",
                    color = colorResource(id = R.color.primary),
                    modifier = Modifier.clickable(onClick = createWorkspace)
                )
            }
        }
    ) {
        when (content) {
            BottomNavi.Home -> HomeScreen(homeViewModel, bottomBarSize, openCalendar, homeUiState)
            BottomNavi.My -> {}
            BottomNavi.Team -> TeamScreen(teamViewModel)
        }

    }
}

@Composable
private fun BottomNavigation(modifier: Modifier, onClick: (selected: BottomNavi) -> Unit) {
    var naviPos: BottomNavi by remember {
        mutableStateOf(BottomNavi.Home)
    }

    BottomNavigation(
        modifier = modifier,
        backgroundColor = colorResource(id = R.color.light_gray_white)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavi::class.sealedSubclasses.forEach { item ->
                val naviItem = item.objectInstance ?: return@forEach
                BottomNaviItem(naviItem = naviItem, selected = naviPos) { selectedPosition ->
                    naviPos = selectedPosition
                    onClick(selectedPosition)
                }
            }
        }
    }
}

@Composable
private fun BottomNaviItem(
    naviItem: BottomNavi,
    selected: BottomNavi,
    onClick: (selected: BottomNavi) -> Unit = {}
) {
    val color = if (naviItem == selected) R.color.primary else R.color.gray

    Column(
        modifier = Modifier
            .clickable(onClick = { if (naviItem != selected) onClick(naviItem) })
            .padding(vertical = 3.dp, horizontal = 12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = naviItem.image),
            contentDescription = null,
            tint = colorResource(id = color)
        )
        Text(text = stringResource(id = naviItem.text), color = colorResource(color), fontSize = 12.sp)
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    MeeronTheme {
        MainScreen()
    }
}