package fourtune.meeron.presentation.ui.home.my

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import forutune.meeron.domain.Const
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.DetailItem
import fourtune.meeron.presentation.ui.common.ProfileImage

sealed interface MyMeeronEvent {
    object EditProfile : MyMeeronEvent
    object EditWorkspace : MyMeeronEvent
    object EditAccount : MyMeeronEvent
    object InquiryOrHomepage : MyMeeronEvent
}

@Composable
fun MyMeeronScreen(
    viewModel: MyMeeronViewModel = hiltViewModel(),
    bottomBarSize: Dp = 90.dp,
    myMeeronEvent: (MyMeeronEvent) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    MyMeeronScreen(
        uiState = uiState,
        bottomBarSize = bottomBarSize,
        event = myMeeronEvent
    )
}

@Composable
private fun MyMeeronScreen(
    uiState: MyMeeronViewModel.UiState,
    bottomBarSize: Dp = 90.dp,
    event: (MyMeeronEvent) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(top = 40.dp, bottom = bottomBarSize)
            .fillMaxWidth()
    ) {
        ProfileItem(uiState.myName, uiState.profileImage)
        ActionItems(event)
    }
}

@Composable
private fun ProfileItem(name: String, image: Any?) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(text = "안녕하세요", fontSize = 21.sp, color = colorResource(id = R.color.black))
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = colorResource(id = R.color.dark_primary),
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(name)
                }
                append("님")
            },
            fontSize = 28.sp,
            color = colorResource(id = R.color.black),
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.padding(25.dp))
        ProfileImage(modifier = Modifier.align(Alignment.CenterHorizontally), image = image)
        Spacer(modifier = Modifier.padding(20.dp))
    }
}

@Composable
private fun ActionItems(event: (MyMeeronEvent) -> Unit = {}) {
    val context = LocalContext.current
    LazyColumn {
        item {
            Divider(thickness = 20.dp, color = colorResource(id = R.color.topbar_color))
        }
        item {
            DetailItem(title = "프로필 관리", onClickDetail = { event(MyMeeronEvent.EditProfile) })
            DetailItem(title = "워크 스페이스 관리", onClickDetail = { event(MyMeeronEvent.EditWorkspace) })
            DetailItem(title = "계정 관리", onClickDetail = { event(MyMeeronEvent.EditAccount) })
        }
        item {
            Divider(thickness = 20.dp, color = colorResource(id = R.color.topbar_color))
        }
        item {
            DetailItem(title = "문의 및 홈페이지", onClickDetail = { event(MyMeeronEvent.InquiryOrHomepage) })
            DetailItem(title = "이용약관", onClickDetail = {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(Const.TermsOfUse)
                    )
                )
            })
            DetailItem(title = "개인정보 처리방침", onClickDetail = {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(Const.PrivacyPolicy)
                    )
                )
            })
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MyMeeronScreen(
        uiState = MyMeeronViewModel.UiState(myName = "zero"),
        bottomBarSize = 90.dp
    )
}