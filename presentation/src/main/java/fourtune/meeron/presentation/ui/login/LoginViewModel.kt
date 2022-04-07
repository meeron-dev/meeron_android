package fourtune.meeron.presentation.ui.login

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import com.kakao.sdk.user.rx
import dagger.hilt.android.lifecycle.HiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.EntryPointType
import forutune.meeron.domain.model.LoginUser
import forutune.meeron.domain.model.MeeronError
import forutune.meeron.domain.usecase.IsFirstVisitUserUseCase
import forutune.meeron.domain.usecase.login.LoginUseCase
import forutune.meeron.domain.usecase.workspace.GetUserWorkspacesUseCase
import forutune.meeron.domain.usecase.workspace.SetCurrentWorkspaceInfoUseCase
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
    private val getUserWorkspaces: GetUserWorkspacesUseCase,
    private val isFirstVisitUser: IsFirstVisitUserUseCase,
    private val setCurrentWorkspaceInfo: SetCurrentWorkspaceInfoUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val entryPointType = savedStateHandle.get<EntryPointType>(Const.EntryPointType) ?: EntryPointType.Normal

    private val _loginSuccess = MutableSharedFlow<Event>()
    fun loginSuccess() = _loginSuccess.asSharedFlow()

    private val _toast = MutableSharedFlow<String?>()
    val toast = _toast.asSharedFlow()

    private val loginContext = CoroutineExceptionHandler { _, throwable ->
        _toast.tryEmit(throwable.message ?: "coroutine error $throwable")
        Timber.tag("🔥zero:coroutine").e("$throwable")
    }

    init {
        viewModelScope.launch(loginContext) {
            runCatching {
                loginUseCase(getMe = { UserApiClient.rx.me().await().toLoginUser() })
            }
                .onFailure {
                    if (it is MeeronError) {
                        _toast.emit(it.errorMessage)
                    } else {
                        _toast.emit(it.message)
                    }
                    Timber.tag("🔥zero:initLogin").w("$it")

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
            val userWorkspaces = getUserWorkspaces()
            val event = if (userWorkspaces.isEmpty()) {
                Event.GoToSignIn
            } else {
                setCurrentWorkspaceInfo(userWorkspaces)
                if (isFirstVisitUser()) Event.ShowOnBoarding
                else Event.GoToHome

            }
            _loginSuccess.emit(event)
        }
    }

    sealed interface Event {
        object GoToSignIn : Event
        object GoToHome : Event
        object ShowOnBoarding : Event
    }
}