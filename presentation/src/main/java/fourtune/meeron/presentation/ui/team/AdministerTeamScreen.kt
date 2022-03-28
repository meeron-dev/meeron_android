package fourtune.meeron.presentation.ui.team

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.CenterTextTopAppBar

@Composable
fun AdministerTeamScreen(onBack:()->Unit) {
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
            Column {
                Text(
                    text = "팀 이름",
                    fontSize = 18.sp,
                    color = colorResource(id = R.color.dark_gray),
                )
            }
        }
    )
}