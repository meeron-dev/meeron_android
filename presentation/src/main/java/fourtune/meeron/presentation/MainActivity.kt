package fourtune.meeron.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import fourtune.meeron.presentation.ui.calendar.CalendarActivity
import fourtune.meeron.presentation.ui.theme.MeeronTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val calendarContract = registerForActivityResult(CalendarActivity.Contract) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MeeronTheme {
                MeeronNavigator {
                    calendarContract.launch("param")
                }
            }
        }
    }
}

