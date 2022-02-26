package fourtune.meeron.presentation.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import fourtune.meeron.presentation.R
import kotlinx.coroutines.flow.collect

@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel(), isLoginSuccess: () -> Unit) {
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.loginSuccess().collect { isLoginSuccess ->
            if (isLoginSuccess) isLoginSuccess()
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .size(300.dp)
                .clickable { viewModel.launchKakaoLogin(context) },
            painter = painterResource(id = R.drawable.button_kakao_login),
            contentDescription = null
        )
        Button(onClick = { viewModel.logout() }) {
            Text(text = "로그아웃")
        }
    }

}