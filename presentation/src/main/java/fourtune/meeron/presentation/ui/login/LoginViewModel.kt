package fourtune.meeron.presentation.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import com.kakao.sdk.user.rx
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.LoginUser
import forutune.meeron.domain.usecase.LoginUseCase
import forutune.meeron.domain.usecase.LogoutUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.await
import timber.log.Timber
import javax.inject.Inject

private fun User.toLoginUser(): LoginUser {
    return LoginUser(
        kakaoAccount?.email.orEmpty(),
        kakaoAccount?.profile?.nickname.orEmpty(),
        kakaoAccount?.profile?.profileImageUrl.orEmpty(),
        "kakao"
    )
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    private val _loginSuccess = MutableSharedFlow<Boolean>()
    fun loginSuccess() = _loginSuccess.asSharedFlow()

    private val loginContext = CoroutineExceptionHandler { _, throwable ->
        Timber.tag("🔥zero:").e("$throwable")
    }

    init {
        viewModelScope.launch(loginContext) {
            val isLoginSuccess =
                runCatching {
                    loginUseCase(getMe = { UserApiClient.rx.me().await().toLoginUser() })
                }.isSuccess
            _loginSuccess.emit(isLoginSuccess)
        }
    }

    fun launchKakaoLogin(context: Context) {
        viewModelScope.launch(loginContext) {
            val isLoginSuccess = runCatching {
                loginUseCase(
                    kakaoLogin = { UserApiClient.rx.loginWithKakaoTalk(context = context).await() },
                    isKakaoLoginAvailable = { UserApiClient.instance.isKakaoTalkLoginAvailable(context) },
                    kakaoLoginWithAccount = { UserApiClient.rx.loginWithKakaoAccount(context).await() },
                    getMe = { UserApiClient.rx.me().await().toLoginUser() }
                )
            }.isSuccess
            _loginSuccess.emit(isLoginSuccess)
        }
    }

    fun logout() {
        viewModelScope.launch(loginContext) {
            logoutUseCase(kakaoLogout = { UserApiClient.rx.unlink().await() })
        }
    }
}