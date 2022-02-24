package fourtune.meeron.presentation.ui.calendar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.CircleBackgroundText
import fourtune.meeron.presentation.ui.theme.MeeronTheme

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CalendarScreen() {
//    Body()
}

@Composable
private fun Body() {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text(
                        text = "나의 캘린더",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.black)
                    )
                }
            )
        }
    ) {
        Column {
            Text(text = "aasd")
        }
    }
}

@Composable
fun CalendarDetail(index: Int = 0) {
    Row {
        CircleBackgroundText(
            modifier = Modifier
                .background(colorResource(id = R.color.primary))
                .size(24.dp),
            "${index + 1}",
            colorResource(id = R.color.white)
        )
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = "title 입니다.",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.dark_primary)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "AM 10:00~ AM 11:30", fontSize = 12.sp, color = colorResource(id = R.color.medium_primary))
                Text(
                    text = "Team name",
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.medium_primary),
                )
            }
        }
    }
}

@Preview
@Composable
private fun CalendarDetailPrev() {
    MeeronTheme {
        CalendarDetail()
    }
}

@Preview
@Composable
private fun CalendarPrev() {
    MeeronTheme {
        CalendarScreen()
    }
}