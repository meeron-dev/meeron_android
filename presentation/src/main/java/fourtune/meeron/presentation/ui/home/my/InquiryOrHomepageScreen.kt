package fourtune.meeron.presentation.ui.home.my

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.text.TextGuideLineItem
import fourtune.meeron.presentation.ui.common.topbar.CenterTextTopAppBar

@Composable
fun InquiryOrHomepageScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = { InquiryOrHomepageTopBar(onBack) },
        content = { InquiryOrHomepageContent() }
    )
}

@Composable
private fun InquiryOrHomepageTopBar(onBack: () -> Unit) {
    CenterTextTopAppBar(
        text = {
            Text(
                text = "문의 및 홈페이지",
                fontSize = 18.sp,
                color = colorResource(id = R.color.black),
                fontWeight = FontWeight.Bold
            )
        },
        onNavigation = onBack
    )
}

@Composable
fun InquiryOrHomepageContent() {
    Column(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 70.dp),
            painter = painterResource(id = R.drawable.ic_picture_plane),
            contentDescription = null
        )
        Column(
            modifier = Modifier
                .background(color = colorResource(id = R.color.topbar_color))
                .fillMaxHeight()
                .padding(vertical = 25.dp, horizontal = 20.dp)
        ) {
            TextGuideLineItem(title = "문의", text = "nuxy0121@daum.net")
            TextGuideLineItem(title = "홈페이지", text = "www.meeron.net")
        }

    }
}