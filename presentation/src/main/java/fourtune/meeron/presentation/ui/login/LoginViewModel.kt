package fourtune.meeron.presentation.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import com.kakao.sdk.user.rx
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.model.LoginUser
import forutune.meeron.domain.usecase.login.LoginUseCase
import forutune.meeron.domain.usecase.login.LogoutUseCase
import forutune.meeron.domain.usecase.workspace.GetUserWorkspacesUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.await
import timber.log.Timber
import javax.inject.Inject

fun User.toLoginUser(): LoginUser {
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
    private val logoutUseCase: LogoutUseCase,
    private val getUserWorkspaces: GetUserWorkspacesUseCase,
) : ViewModel() {
    private val _loginSuccess = MutableSharedFlow<Event>()
    fun loginSuccess() = _loginSuccess.asSharedFlow()

    private val _toast = MutableSharedFlow<String?>()
    val toast = _toast.asSharedFlow()

    private val loginContext = CoroutineExceptionHandler { _, throwable ->
        _toast.tryEmit(throwable.message ?: "coroutine error $throwable")
        Timber.tag("🔥zero:").e("$throwable")
    }

    init {
        viewModelScope.launch(loginContext) {
            runCatching {
                loginUseCase(getMe = { UserApiClient.rx.me().await().toLoginUser() })
            }
                .onFailure {
                    Timber.tag("🔥zero:initLogin").w("$it")
                    _toast.emit(it.message)
                }.onSuccess {
                    redirectLoginEvent()
                }

        }
    }

    fun launchKakaoLogin(context: Context) {
        viewModelScope.launch(loginContext) {
            runCatching {
                loginUseCase(
                    kakaoLogin = { UserApiClient.rx.loginWithKakaoTalk(context = context).await() },
                    isKakaoLoginAvailable = { UserApiClient.instance.isKakaoTalkLoginAvailable(context) },
                    kakaoLoginWithAccount = { UserApiClient.rx.loginWithKakaoAccount(context).await() },
                    getMe = { UserApiClient.rx.me().await().toLoginUser() }
                )
            }
                .onFailure {
                    Timber.tag("🔥zero:launchKakaoLogin").w("$it")
                    _toast.emit(it.message)
                }.onSuccess {
                    redirectLoginEvent()
                }


        }
    }

    private fun redirectLoginEvent() {
        viewModelScope.launch {
            val event = if (getUserWorkspaces().isEmpty()) {
                Event.NoWorkspace
            } else {
                Event.HasWorkspace
            }
            _loginSuccess.emit(event)
        }
    }

    fun logout() {
        viewModelScope.launch(loginContext) {
            logoutUseCase(kakaoLogout = { UserApiClient.rx.unlink().await() })
        }
    }

    sealed interface Event {
        object NoWorkspace : Event
        object HasWorkspace : Event
    }
}