package fourtune.meeron.presentation

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import fourtune.meeron.presentation.navigator.MeeronNavigator
import fourtune.meeron.presentation.ui.theme.MeeronTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val rootView by lazy { LinearLayout(this) }
    private val viewModel by viewModels<MainViewModel>()
    private var isReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(
            rootView, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        )
        viewModel.event.onEach {
            isReady = true
        }.launchIn(lifecycleScope)

        setContent {
            MeeronTheme {
                MeeronNavigator()
            }
        }
    }

}

