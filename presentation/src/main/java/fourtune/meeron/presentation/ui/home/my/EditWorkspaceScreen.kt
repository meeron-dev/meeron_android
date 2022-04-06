package fourtune.meeron.presentation.ui.home.my

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.text.LinkText
import fourtune.meeron.presentation.ui.common.topbar.CenterTextTopAppBar

@Composable
fun EditWorkspaceScreen(viewModel: EditWorkspaceViewModel = hiltViewModel(), goToMyMeeron: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    EditWorkspaceScreen(uiState, goToMyMeeron)

}

@Composable
private fun EditWorkspaceScreen(
    uiState: EditWorkspaceViewModel.UiState,
    goToMyMeeron: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            EditWorkspaceTopBar(goToMyMeeron)
        },
        content = {
            EditWorkspaceContent(uiState)
        }
    )
}

@Composable
private fun EditWorkspaceTopBar(goToMyMeeron: () -> Unit) {
    CenterTextTopAppBar(
        text = {
            Text(
                text = "워크스페이스 관리",
                fontSize = 18.sp,
                color = colorResource(id = R.color.black),
                fontWeight = FontWeight.Bold
            )
        },
        onNavigation = goToMyMeeron
    )
}

@Composable
private fun EditWorkspaceContent(uiState: EditWorkspaceViewModel.UiState) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Column(modifier = Modifier.padding(vertical = 45.dp)) {
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = "공유링크",
                fontSize = 20.sp,
                color = colorResource(id = R.color.dark_gray),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.padding(25.dp))
            Text(
                text = "초대 링크를 통해\n구성원을 초대해 보세요",
                fontSize = 18.sp,
                color = colorResource(id = R.color.dark_gray),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.padding(9.dp))
            Text(text = uiState.link, fontSize = 15.sp, color = colorResource(id = R.color.gray))
            Spacer(modifier = Modifier.padding(2.dp))
            Text(
                modifier = Modifier.clickable {
                    clipboardManager.setText(annotatedString = AnnotatedString(uiState.link))
                    Toast.makeText(context, "복사 되었습니다.", Toast.LENGTH_SHORT).show()
                },
                text = LinkText("복사하기"),
                fontSize = 13.sp,
                color = colorResource(id = R.color.dark_primary)
            )
        }
        Spacer(modifier = Modifier.padding(20.dp))
        Divider(thickness = 20.dp, color = colorResource(id = R.color.topbar_color))
        Spacer(modifier = Modifier.padding(20.dp))
    }
}


@Preview
@Composable
private fun Preview() {
    EditWorkspaceScreen(uiState = EditWorkspaceViewModel.UiState(link = "www.google.com"), {})
}