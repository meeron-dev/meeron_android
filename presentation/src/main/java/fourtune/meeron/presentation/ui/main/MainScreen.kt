package fourtune.meeron.presentation.ui.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.Dot
import fourtune.meeron.presentation.ui.theme.MeeronTheme

enum class BottomNavi(@DrawableRes val image: Int, @StringRes val text: Int) {
    HOME(R.drawable.ic_navi_home, R.string.home),
    TEAM(R.drawable.ic_navi_team, R.string.team),
    PLUS(R.drawable.ic_navi_plus, R.string.create_conference),
    MY(R.drawable.ic_navi_door, R.string.my_merron)
}

enum class TabItems(@StringRes val text: Int) {
    TODO(R.string.coming_soon), COMPLETE(R.string.complete)
}

sealed interface MainEvent {
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen(openCalendar: () -> Unit = {}) {
    val pagerState = rememberPagerState(0)
    val bottomBarSize = 90.dp
    var tabPos by rememberSaveable {
        mutableStateOf(TabItems.TODO.ordinal)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { /*TODO OPEN SIDE DRAWER*/ }) {
                        Image(painter = painterResource(id = R.drawable.ic_home_menu), contentDescription = "title")
                    }
                },
                title = { TitleText(Modifier.fillMaxWidth()) },
                actions = { ActionRow(Modifier.padding(horizontal = 18.dp)) },
            )
        },
        bottomBar = {
            BottomNavigation(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                    .height(bottomBarSize)
            )
        }
    ) {
        Column(
            Modifier
                .fillMaxHeight()
                .padding(bottom = bottomBarSize + 16.dp)
        ) {
            CalendarTitle(modifier = Modifier.padding(vertical = 28.dp, horizontal = 4.dp), openCalendar = openCalendar)
            MainTab(selectedTabIndex = tabPos, onClick = { selectedPosition -> tabPos = selectedPosition })
            Spacer(modifier = Modifier.padding(6.dp))
            HorizontalPager(
                modifier = Modifier.fillMaxHeight(),
                count = 20,
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 50.dp),
                itemSpacing = 14.dp
            ) {
                PagerItem()
            }
        }
    }
}

@Composable
private fun BottomNavigation(modifier: Modifier) {
    var naviPos by rememberSaveable {
        mutableStateOf(BottomNavi.HOME.ordinal)
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
            BottomNavi.values().forEach { item ->
                BottomNaviItem(naviItem = item, selected = naviPos) { selectedPosition ->
                    naviPos = selectedPosition
                }
            }
        }
    }
}

@Composable
private fun TitleText(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.fourtune),
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ActionRow(modifier: Modifier) {
    Row(modifier = modifier) {
        Image(painter = painterResource(id = R.drawable.ic_home_search), contentDescription = "search")
        Spacer(modifier = Modifier.padding(5.dp))
        Image(painter = painterResource(id = R.drawable.ic_home_bell), contentDescription = "alert")
    }
}

@Preview
@Composable
private fun PagerItemPrev() {
    MeeronTheme {
        PagerItem()
    }
}

@Composable
private fun PagerItem() {
    Card(modifier = Modifier.fillMaxHeight(), elevation = 1.dp) {
        //content
        Column(
            modifier = Modifier
                .padding(top = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "1차 발표sasddsasddasadsdsadsaasdadsdasadsasdadsasdsasdsadadsasddasas123123123\n컨텐츠 구상",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.black),
                    overflow = TextOverflow.Clip
                )
                Spacer(modifier = Modifier.padding(3.dp))
                Text(
                    text = "2022년 2월 4일\n9:00AM~1:00PM",
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.dark_gray)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.conference_plan),
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.gray)
                    )
                    Text(text = "주관 오퍼레이션 팀", fontSize = 13.sp, color = colorResource(id = R.color.gray))
                }
                Column {
                    Row {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        Text(text = "5", color = colorResource(id = R.color.gray), fontSize = 12.sp)
                    }
                    Row {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        Text(text = "5", color = colorResource(id = R.color.gray), fontSize = 12.sp)
                    }
                    Row {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        Text(text = "5", color = colorResource(id = R.color.gray), fontSize = 12.sp)
                    }
                    Row {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        Text(text = "5", color = colorResource(id = R.color.gray), fontSize = 12.sp)
                    }
                    //o x ?
                }
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Box(
                modifier = Modifier
                    .background(color = colorResource(id = R.color.primary))
                    .fillMaxWidth()
                    .padding(vertical = 34.dp, horizontal = 24.dp)
            ) {
                Text(text = "앙 기무치", fontSize = 14.sp)
            }
        }

    }

}

@Composable
private fun BottomNaviItem(naviItem: BottomNavi, selected: Int, onClick: (selected: Int) -> Unit = {}) {
    val color = remember(naviItem.ordinal == selected) {
        if (naviItem.ordinal == selected) R.color.primary else R.color.gray
    }
    Column(
        modifier = Modifier
            .clickable(onClick = { onClick(naviItem.ordinal) })
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

@Composable
private fun CalendarTitle(modifier: Modifier, openCalendar: () -> Unit = {}) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = openCalendar) {
            Image(imageVector = Icons.Default.DateRange, contentDescription = null)
        }
        Text(text = "현재 날짜 들어가면 됨", fontSize = 22.sp, color = colorResource(id = R.color.dark_gray))
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun MainTab(modifier: Modifier = Modifier, selectedTabIndex: Int, onClick: (selected: Int) -> Unit) {
    ScrollableTabRow(modifier = modifier, selectedTabIndex = selectedTabIndex, edgePadding = 20.dp) {
        TabItems.values().forEachIndexed { index, tabItems ->
            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .clickable { onClick(index) }
            ) {
                Text(
                    text = stringResource(id = tabItems.text),
                    color = colorResource(id = if (selectedTabIndex == index) R.color.dark_primary else R.color.gray),
                    fontSize = 15.sp,
                    fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium
                )
                Dot(colorResource(id = R.color.dark_primary), 4.dp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Main() {
    MeeronTheme {
        MainScreen()
    }
}