package fourtune.meeron

import android.app.Application
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MeeronApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        TransferNetworkLossHandler.getInstance(this)
        KakaoSdk.init(this, BuildConfig.APP_KEY)
    }
}