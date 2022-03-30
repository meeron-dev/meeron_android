package fourtune.meeron.presentation

import android.os.Bundle
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import fourtune.meeron.presentation.navigator.MeeronNavigator
import fourtune.meeron.presentation.navigator.Navigate
import fourtune.meeron.presentation.ui.theme.MeeronTheme
import kotlinx.coroutines.flow.collectLatest
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

        rootView.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (isReady) {
                        rootView.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            }
        )

        lifecycleScope.launchWhenCreated {
            viewModel.toast.collectLatest {
                Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.event.onEach { event ->
            val navigate = when (event) {
                MainViewModel.Event.GoToHome -> Navigate.Main
                MainViewModel.Event.GoToLogin -> Navigate.Login
                MainViewModel.Event.GoToCreateOrJoin -> Navigate.CreateWorkspace.CreateOrJoin
            }
            isReady = true
            setContent {
                MeeronTheme {
                    MeeronNavigator(navigate)
                }
            }
        }.launchIn(lifecycleScope)


    }

}
