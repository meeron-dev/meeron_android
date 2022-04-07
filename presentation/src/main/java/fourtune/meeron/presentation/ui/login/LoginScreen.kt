package fourtune.meeron.presentation.ui.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import fourtune.meeron.presentation.R
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    goToHome: () -> Unit = {},
    goToSignIn: () -> Unit = {},
    showOnBoarding: () -> Unit = {}
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.toast.collect {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }
    LaunchedEffect(key1 = true) {
        viewModel.loginSuccess().collectLatest { isLoginSuccess ->
            when (isLoginSuccess) {
                LoginViewModel.Event.GoToHome -> goToHome()
                LoginViewModel.Event.GoToSignIn -> goToSignIn()
                LoginViewModel.Event.ShowOnBoarding -> showOnBoarding()
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 23.dp, vertical = 90.dp),
    ) {
        Image(
            modifier = Modifier.align(Alignment.Center),
            painter = painterResource(id = R.drawable.ic_meeron_symbol),
            contentDescription = null
        )

        Image(
            modifier = Modifier
                .clickable { viewModel.launchKakaoLogin(context) }
                .align(Alignment.BottomCenter),
            painter = painterResource(id = R.drawable.ic_kakao_login),
            contentDescription = null
        )

    }

}

@Preview
@Composable
private fun Preview() {
    LoginScreen {

    }
}