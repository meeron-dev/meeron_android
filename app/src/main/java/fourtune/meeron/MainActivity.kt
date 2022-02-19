package fourtune.meeron

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import fourtune.meeron.ui.theme.MeeronTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MeeronTheme {
                // A surface container using the 'background' color from the theme
            }
        }
    }
}

