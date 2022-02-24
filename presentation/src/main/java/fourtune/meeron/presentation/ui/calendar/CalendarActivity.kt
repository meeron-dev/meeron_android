package fourtune.meeron.presentation.ui.calendar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Spanned
import android.text.style.UnderlineSpan
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.toSpannable
import androidx.lifecycle.lifecycleScope
import com.prolificinteractive.materialcalendarview.CalendarDay
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.databinding.ActivityCalendarBinding
import fourtune.meeron.presentation.ui.calendar.decorator.EventDecorator
import fourtune.meeron.presentation.ui.calendar.decorator.SelectionDecorator
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class CalendarActivity : AppCompatActivity() {
    private val viewModel by viewModels<CalendarViewModel>()
    private val binding: ActivityCalendarBinding by lazy { ActivityCalendarBinding.inflate(layoutInflater) }

    private val selectionDecor by lazy { SelectionDecorator(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.currentDay().onEach { day ->
            setCurrentTitle(day.year, day.month)
        }.launchIn(lifecycleScope)


        binding.showAll.text.toSpannable().apply {
            setSpan(UnderlineSpan(), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        with(binding.calendar) {
            topbarVisible = false
            setDateTextAppearance(R.style.CalendarTextAppearance)
            setWeekDayTextAppearance(R.style.CalendarWeekAppearance)
            setBackgroundColor(ContextCompat.getColor(this@CalendarActivity, R.color.background))
            addDecorators(
                EventDecorator(
                    context,
                    CalendarDay.from(
                        viewModel.currentDay().value.year,
                        viewModel.currentDay().value.month,
                        viewModel.currentDay().value.day
                    )
                ),
                selectionDecor
            )
            setOnDateChangedListener { widget, date, selected ->
                selectionDecor.setDate(date)
                widget.invalidateDecorators()
            }
            setOnMonthChangedListener { _, date ->
                setCurrentTitle(date.year, date.month)
            }
        }

    }

    private fun setCurrentTitle(year: Int, month: Int) {
        binding.month.text = String.format("%d 월", month)
        binding.year.text = year.toString()
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, CalendarActivity::class.java)
    }

    object Contract : ActivityResultContract<String, Boolean>() {
        override fun createIntent(context: Context, input: String?): Intent {
            return getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return true
        }
    }
}