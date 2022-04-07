package fourtune.meeron.presentation.ui

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.MeeronProgressIndicator
import fourtune.meeron.presentation.ui.common.MeeronSingleButton
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun DynamicLinkEntryScreen(
    viewModel: DynamicLinkEntryViewModel = hiltViewModel(),
    goToLogin: () -> Unit,
    goToTOS: () -> Unit,
    goToCreateProfile: () -> Unit
) {
    val activity = LocalContext.current as? Activity
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    BackHandler {
        activity?.finish()
    }

    LaunchedEffect(key1 = currentComposer) {
        viewModel.toast.onEach {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }.launchIn(this)

        viewModel.event.collectLatest { event ->
            when (event) {
                DynamicLinkEntryViewModel.Event.GoToTOS -> goToTOS()
                DynamicLinkEntryViewModel.Event.GoToCreateProfile -> goToCreateProfile()
            }
        }
    }
    when (uiState) {
        DynamicLinkEntryViewModel.UiState.Loading -> MeeronProgressIndicator(showLoading = true)
        DynamicLinkEntryViewModel.UiState.AlreadyJoinOrDeleted -> {
            Content(
                title = "이미 참여중인 워크스페이스거나\n사라진 워크스페이스 입니다.",
                subTitle = "도움이 필요할 시\n문의 부탁 드립니다.\nnuxy0121@daum.net",
                buttonText = "확인"
            ) { activity?.finish() }
        }
        DynamicLinkEntryViewModel.UiState.NotFound -> {
            Content(
                title = "귀하의 회원 정보가\n조회되지 않습니다.",
                subTitle = "회원가입 후 다시 시도해주시길 바랍니다.",
                buttonText = "가입하러 가기"
            ) { goToLogin() }
        }
    }
}

@Composable
private fun Content(title: String = "", subTitle: String = "", buttonText: String = "", onClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 38.dp, vertical = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.padding(20.dp))
            Image(painter = painterResource(id = R.drawable.ic_illustration_error), contentDescription = null)
            Spacer(modifier = Modifier.padding(30.dp))
            Text(
                text = title,
                fontSize = 18.sp,
                color = colorResource(id = R.color.black),
                textAlign = TextAlign.Center,
                lineHeight = 33.sp
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Text(
                text = subTitle,
                fontSize = 15.sp,
                color = colorResource(id = R.color.light_gray),
                lineHeight = 25.sp,
                textAlign = TextAlign.Center
            )
        }
        MeeronSingleButton(onClick = onClick, enable = true, text = buttonText)

    }
}

@Preview
@Composable
private fun Preview() {
    Content()
}