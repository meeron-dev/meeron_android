package fourtune.meeron.presentation.ui.home.my

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import fourtune.meeron.presentation.R
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
    val workspaceInfo by viewModel.workspaceInfo.collectAsState()

    var logoutDialog by remember {
        mutableStateOf(false)
    }

    var withdrawalDialog by remember {
        mutableStateOf(false)
    }

    if (logoutDialog) {
        LogoutDialog(
            onDismissRequest = { logoutDialog = it },
            onLogout = { viewModel.logout(goToLogin) }
        )
    }
    if (withdrawalDialog) {
        WithdrawalDialog(
            workspaceName = workspaceInfo.workSpaceName,
            onDismissRequest = { withdrawalDialog = it },
            onWithdrawal = { viewModel.withdrawal() }
        )
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
private fun WithdrawalDialog(
    workspaceName: String,
    onDismissRequest: (Boolean) -> Unit,
    onWithdrawal: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest(false) }) {

        Column(
            modifier = Modifier.background(colorResource(id = R.color.white), shape = RoundedCornerShape(5.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 12.dp, start = 17.dp),
                text = workspaceName,
                fontSize = 16.sp,
                color = colorResource(id = R.color.dark_gray),
                fontWeight = FontWeight.Medium
            )
            Column(
                modifier = Modifier.padding(vertical = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "정말 회원 탈퇴하시겠습니까?",
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.dark_gray),
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.padding(3.dp))
                Text(
                    text = "관리 중인 워크스페이스가 삭제됩니다.",
                    fontSize = 13.sp,
                    color = colorResource(id = R.color.gray),
                )
            }
            DialogButton(onDismissRequest, "탈퇴하기", onWithdrawal)
        }
    }
}

@Composable
private fun LogoutDialog(
    onDismissRequest: (Boolean) -> Unit,
    onLogout: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest(false) }) {
        Column(
            modifier = Modifier.background(colorResource(id = R.color.white), shape = RoundedCornerShape(5.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(vertical = 80.dp),
                text = "로그아웃 하시겠습니까?",
                fontSize = 16.sp,
                color = colorResource(id = R.color.dark_gray),
                fontWeight = FontWeight.Medium
            )
            DialogButton(onDismissRequest, "로그아웃", onLogout)
        }
    }
}

@Composable
private fun DialogButton(onDismissRequest: (Boolean) -> Unit, actionText: String, onAction: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            modifier = Modifier
                .background(color = colorResource(id = R.color.dark_gray_white))
                .weight(1f),
            onClick = { onDismissRequest(false) },
            contentPadding = PaddingValues(vertical = 20.dp)
        ) {
            Text(
                text = "취소",
                fontSize = 16.sp,
                color = colorResource(id = R.color.dark_gray),
            )
        }
        Divider(modifier = Modifier.width(1.dp))
        TextButton(
            modifier = Modifier
                .background(color = colorResource(id = R.color.dark_gray_white))
                .weight(1f),
            onClick = { onDismissRequest(false);onAction() },
            contentPadding = PaddingValues(vertical = 20.dp)
        ) {
            Text(
                text = actionText,
                fontSize = 16.sp,
                color = colorResource(id = R.color.dark_gray),
            )
        }
    }
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