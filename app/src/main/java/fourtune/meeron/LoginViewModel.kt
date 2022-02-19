package fourtune.meeron

import android.content.Context
import androidx.lifecycle.ViewModel
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.rx
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    init {
        UserApiClient.rx.me()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    Timber.tag("🔥zero:").e("$it")
                },
                onSuccess = { user ->
                    Timber.tag("🔥zero:me").d("$user")
                }
            )
            .addTo(compositeDisposable)
    }

    fun launchKakaoLogin(context: Context) {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {

            UserApiClient.rx.loginWithKakaoTalk(context = context)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = { token ->
                        Timber.tag("🔥zero:loginWithKakaoTalk").d("${token.accessToken}")
                    },
                    onError = { Timber.tag("🔥zero:launchKakaoLogin").e("$it") }
                )
                .addTo(compositeDisposable)
        } else {
            UserApiClient.rx.loginWithKakaoAccount(context)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        Timber.tag("🔥zero:loginWithKakaoAccount").d("")
                    },
                    onError = { Timber.tag("🔥zero:launchKakaoLogin").e("$it") }
                ).addTo(compositeDisposable)
        }
    }

    fun logout() {
        UserApiClient.rx.unlink()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy {
                Timber.tag("🔥zero:logout").d("logout")
            }.addTo(compositeDisposable)
    }
}