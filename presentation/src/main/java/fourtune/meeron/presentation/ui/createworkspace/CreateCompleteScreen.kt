package fourtune.meeron.presentation.ui.createworkspace

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.MeeronSingleButtonBackGround
import fourtune.meeron.presentation.ui.common.topbar.CenterTextTopAppBar

@Composable
fun CreateCompleteScreen(
    viewModel: CreateCompleteViewModel = hiltViewModel(),
    onComplete: () -> Unit = {},
    showOnBoarding: () -> Unit = {}
) {
    val clipboardManager = LocalClipboardManager.current
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterTextTopAppBar(text = "워크 스페이스 생성")
        },
        content = {
            MeeronSingleButtonBackGround(
                text = "다음에 하기",
                onClick = {
                    if (uiState.showOnBoarding) showOnBoarding()
                    else onComplete()
                }
            ) {
                Column {

                    Text(text = "워크 스페이스 생성이\n완료됐어요!", fontSize = 25.sp, color = colorResource(id = R.color.black))
                    Spacer(modifier = Modifier.padding(39.dp))
                    Text(
                        text = "초대 링크를 통해\n구성원을 초대해 보세요",
                        fontSize = 18.sp,
                        color = colorResource(id = R.color.dark_gray)
                    )
                    Spacer(modifier = Modifier.padding(7.dp))
                    Text(
                        text = "워크 스페이스 관리 페이지에서\n언제든 링크를 확인할 수 있어요.",
                        fontSize = 13.sp,
                        color = colorResource(id = R.color.light_gray)
                    )
                    Spacer(modifier = Modifier.padding(17.dp))
                    Text(text = uiState.link, fontSize = 15.sp, color = colorResource(id = R.color.gray))
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        modifier = Modifier.clickable {
                            clipboardManager.setText(annotatedString = AnnotatedString(uiState.link))
                            Toast.makeText(context, "복사 되었습니다.", Toast.LENGTH_SHORT).show()
                        },
                        text = buildAnnotatedString {
                            val text = "복사하기"
                            append(text)
                            addStyle(SpanStyle(textDecoration = TextDecoration.Underline), 0, text.length)
                        },
                        fontSize = 13.sp,
                        color = colorResource(id = R.color.dark_primary)
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun Preview() {
    CreateCompleteScreen()
}