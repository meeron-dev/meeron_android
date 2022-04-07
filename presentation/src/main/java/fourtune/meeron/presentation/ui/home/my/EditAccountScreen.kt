package fourtune.meeron.presentation.ui.home.my

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.dialog.MultiTextDialog
import fourtune.meeron.presentation.ui.common.dialog.SingleTextDialog
import fourtune.meeron.presentation.ui.common.topbar.CenterTextTopAppBar

internal sealed interface EditAccountEvent {
    object Logout : EditAccountEvent
    object Withdrawal : EditAccountEvent
}

@Composable
fun EditAccountScreen(
    viewModel: EditAccountViewModel = hiltViewModel(),
    goToMyMeeron: () -> Unit = {},
    goToLogin: () -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    var logoutDialog by remember {
        mutableStateOf(false)
    }

    var withdrawalDialog by remember {
        mutableStateOf(false)
    }

    if (logoutDialog) {
        SingleTextDialog(
            buttonText = "로그아웃",
            text = "로그아웃 하시겠습니까?",
            onDismissRequest = { logoutDialog = it }
        ) {
            viewModel.logout {
                Toast.makeText(context, "로그아웃되셨습니다.", Toast.LENGTH_SHORT).show()
                goToLogin()
            }
        }
    }
    if (withdrawalDialog) {
        MultiTextDialog(
            title = "정말 회원 탈퇴하시겠습니까?",
            text = if (uiState.me.workspaceAdmin) "관리 중인 워크스페이스가 삭제됩니다." else "저장되어 있던 정보가 삭제될 수 있습니다.",
            topText = uiState.workspaceInfo.workSpaceName,
            "탈퇴하기",
            onDismissRequest = { withdrawalDialog = it }
        ) {
            viewModel.withdrawal {
                Toast.makeText(context, "회원 탈퇴 되셨습니다.", Toast.LENGTH_SHORT).show()
                goToLogin()
            }
        }
    }
    EditAccountScreen(
        goToMyMeeron = goToMyMeeron,
        event = { event ->
            when (event) {
                EditAccountEvent.Logout -> {
                    logoutDialog = true
                }
                EditAccountEvent.Withdrawal -> {
                    withdrawalDialog = true
                }
            }
        }
    )

}

@Composable
private fun EditAccountScreen(goToMyMeeron: () -> Unit, event: (EditAccountEvent) -> Unit) {
    Scaffold(
        topBar = {
            EditAccountTopBar(goToMyMeeron)
        },
        content = {
            EditAccountContent(event)
        }
    )
}

@Composable
private fun EditAccountTopBar(goToMyMeeron: () -> Unit) {
    CenterTextTopAppBar(
        text = {
            Text(
                text = "계정 관리",
                fontSize = 18.sp,
                color = colorResource(id = R.color.black),
                fontWeight = FontWeight.Bold
            )
        },
        onNavigation = goToMyMeeron
    )
}


@Composable
private fun EditAccountContent(event: (EditAccountEvent) -> Unit = {}) {
    Column(Modifier.padding(20.dp)) {
        Text(
            modifier = Modifier
                .padding(vertical = 13.dp)
                .clickable { event(EditAccountEvent.Logout) },
            text = "로그아웃",
            fontSize = 16.sp,
            color = colorResource(id = R.color.gray)
        )
        Text(
            modifier = Modifier
                .padding(vertical = 13.dp)
                .clickable { event(EditAccountEvent.Withdrawal) },
            text = "회원 탈퇴",
            fontSize = 16.sp,
            color = colorResource(id = R.color.gray)
        )
    }
}

@Preview
@Composable
private fun Preview() {
    EditAccountScreen {

    }
}