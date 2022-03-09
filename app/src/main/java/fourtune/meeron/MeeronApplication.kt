package fourtune.meeron

import android.app.Application
import android.widget.Toast
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MeeronApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val keyHash = Utility.getKeyHash(this)
        KakaoSdk.init(this, BuildConfig.APP_KEY)
        Toast.makeText(this, keyHash, Toast.LENGTH_SHORT).show()
    }
}