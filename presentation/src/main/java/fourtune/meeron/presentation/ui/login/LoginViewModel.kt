package fourtune.meeron.presentation.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.rx
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.await
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(

) : ViewModel() {

    private val loginExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.tag("🔥zero:").e("$throwable")
    }

    init {
        viewModelScope.launch(loginExceptionHandler) {
        }
    }

    fun launchKakaoLogin(context: Context) {
        viewModelScope.launch(loginExceptionHandler) {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                val token = UserApiClient.rx.loginWithKakaoTalk(context = context).await()
                Timber.tag("🔥zero:launchKakaoLogin").d("$token")
            } else {
                val token = UserApiClient.rx.loginWithKakaoAccount(context).await()
                Timber.tag("🔥zero:launchKakaoLogin").d("$token")
            }
        }
    }

    fun logout() {
        viewModelScope.launch(loginExceptionHandler) {
            UserApiClient.rx.unlink().await()
        }
    }
}