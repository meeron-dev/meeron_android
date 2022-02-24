package fourtune.meeron.presentation.ui.calendar.decorator

import android.content.Context
import android.graphics.Typeface
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import fourtune.meeron.presentation.R

class SelectionDecorator(private val context: Context) :
    DayViewDecorator {
    private var decorDay: CalendarDay = CalendarDay.today()

    override fun shouldDecorate(day: CalendarDay?): Boolean = day == decorDay

    override fun decorate(view: DayViewFacade?) {
        view?.setSelectionDrawable(ContextCompat.getDrawable(context, R.drawable.inset)!!)
        view?.addSpan(ForegroundColorSpan(context.getColor(R.color.white)))
        view?.addSpan(StyleSpan(Typeface.BOLD))
    }

    fun setDate(day: CalendarDay) {
        decorDay = day
    }
}