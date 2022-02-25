package fourtune.meeron.presentation.ui.calendar.all

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
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
import fourtune.meeron.presentation.ui.common.Dot
import fourtune.meeron.presentation.ui.theme.MeeronTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowAllScreen(viewModel: ShowAllViewModel = hiltViewModel(), onAction: () -> Unit = {}) {
    Scaffold(topBar = {
        TopAppBar(title = {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "캘린더 전체보기", fontSize = 18.sp, color = colorResource(id = R.color.black))
                Text(text = "나의 캘린더", fontSize = 16.sp, color = colorResource(id = R.color.gray))
            }
        }, actions = {
            IconButton(onClick = onAction) {
                Image(painter = painterResource(id = R.drawable.ic_calender_close), contentDescription = null)
            }
        })
    }) {
        Row {
            Column {
                YearItem()
            }
            LazyVerticalGrid(cells = GridCells.Fixed(2)) {
//                items(12)
            }
        }
    }
}

@Composable
private fun YearItem() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "2022년",
            color = colorResource(id = R.color.primary),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "50개의 회의",
            color = colorResource(id = R.color.dark_gray),
            fontSize = 12.sp
        )
    }
}

@Composable
private fun MonthItem() {
    Column(verticalArrangement = Arrangement.Center) {
        Row {
            Text(
                text = "1월",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.dark_gray)
            )
            Dot(color = colorResource(id = R.color.primary), size = 6.dp)
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Text(text = "10개의 회의", fontSize = 12.sp, color = colorResource(id = R.color.gray))
    }
}

@Preview
@Composable
private fun DetailItemPrv() {
    MeeronTheme {
        MonthItem()
    }
}

@Preview
@Composable
private fun ShowAllPrev() {
    MeeronTheme {
        ShowAllScreen()
    }
}