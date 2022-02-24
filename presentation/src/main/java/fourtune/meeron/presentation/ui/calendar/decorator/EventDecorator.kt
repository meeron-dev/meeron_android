package fourtune.meeron.presentation.ui.calendar.decorator

import android.content.Context
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import fourtune.meeron.presentation.R

class EventDecorator(private val context: Context, private val decorDay: CalendarDay) : DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay?): Boolean = day == decorDay

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(10f, ContextCompat.getColor(context, R.color.primary)))
    }

}