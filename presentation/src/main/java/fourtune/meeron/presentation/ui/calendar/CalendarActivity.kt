package fourtune.meeron.presentation.ui.calendar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fourtune.meeron.presentation.databinding.ActivityCalendarBinding

class CalendarActivity : AppCompatActivity() {
    private val binding: ActivityCalendarBinding by lazy { ActivityCalendarBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, CalendarActivity::class.java)
    }
}