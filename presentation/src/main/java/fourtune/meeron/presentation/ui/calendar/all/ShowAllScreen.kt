package fourtune.meeron.presentation.ui.calendar.all

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
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
import forutune.meeron.domain.model.MonthCount
import forutune.meeron.domain.model.YearCount
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.Dot
import fourtune.meeron.presentation.ui.theme.MeeronTheme

@Composable
fun ShowAllScreen(viewModel: ShowAllViewModel = hiltViewModel(), onAction: () -> Unit = {}) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(topBar = {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "캘린더 전체보기", fontSize = 18.sp, color = colorResource(id = R.color.black))
                Spacer(modifier = Modifier.padding(4.dp))
                Text(text = "나의 캘린더", fontSize = 16.sp, color = colorResource(id = R.color.gray))
            }
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(horizontal = 16.dp), onClick = onAction
            ) {
                Image(painter = painterResource(id = R.drawable.ic_calender_close), contentDescription = null)
            }
        }
    }) {
        ShowAllScreen(
            yearCounts = uiState.yearCounts,
            monthCounts = uiState.monthCounts,
            event = { event ->
                when (event) {
                    is ShowAllViewModel.Event.ClickYear -> viewModel.loadMonthCounts(event.year)
                }
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ShowAllScreen(
    yearCounts: List<YearCount>,
    monthCounts: List<MonthCount>,
    event: (ShowAllViewModel.Event) -> Unit = {}
) {
    Row {
        Column {
            LazyColumn(contentPadding = PaddingValues(vertical = 25.dp)) {
                items(items = yearCounts) {
                    YearItem(year = it.year, count = it.count, event = event)
                }
            }

        }
        LazyVerticalGrid(
            modifier = Modifier
                .background(color = colorResource(id = R.color.light_gray_white))
                .fillMaxHeight(),
            cells = GridCells.Fixed(2),
            contentPadding = PaddingValues(vertical = 15.dp)
        ) {
            items(monthCounts) {
                MonthItem(month = it.month, count = it.count, isNew = true)
            }
        }
    }
}

@Composable
private fun YearItem(modifier: Modifier = Modifier, year: Int, count: Int, event: (ShowAllViewModel.Event) -> Unit) {
    Column(
        modifier = modifier
            .clickable { event(ShowAllViewModel.Event.ClickYear(year)) }
            .padding(vertical = 25.dp, horizontal = 33.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${year}년",
            color = colorResource(id = R.color.primary),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.padding(3.dp))
        Text(
            text = "${count}개의 회의",
            color = colorResource(id = R.color.dark_gray),
            fontSize = 12.sp
        )
    }
}

@Composable
private fun MonthItem(modifier: Modifier = Modifier, month: Int = 0, count: Int = 0, isNew: Boolean = false) {
    Column(
        modifier = modifier.padding(vertical = 15.dp, horizontal = 25.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            Text(
                text = "${month}월",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.dark_gray)
            )
            if (isNew) Dot(color = colorResource(id = R.color.primary), size = 6.dp)
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Text(text = "${count}개의 회의", fontSize = 12.sp, color = colorResource(id = R.color.gray))
    }
}

@Preview
@Composable
private fun MonthItemPrev() {
    MeeronTheme {
        MonthItem()
    }
}

@Preview
@Composable
private fun YearItemPrev() {
    MeeronTheme {
        YearItem(year = 2022, count = 50, event = {})
    }
}

@Preview
@Composable
private fun ShowAllPrev() {
    MeeronTheme {
        ShowAllScreen()
    }
}