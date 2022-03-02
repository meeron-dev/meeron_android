package forutune.meeron.domain.usecase.time

import forutune.meeron.domain.model.Time
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class GetTimeUseCase @Inject constructor(
    private val calendar: Calendar
) {

    operator fun invoke(hour: Int, minute: Int): Time {
        val time: Date = calendar.apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }.time
        val formattedTime: String = SimpleDateFormat(TIME_FORMAT, Locale.US).format(time)
        val timeSection = formattedTime.split(" ")
        return Time(time = timeSection[TIME], hourOfDay = timeSection[HOUR_OF_DAY])
    }

    companion object {
        private const val TIME = 0
        private const val HOUR_OF_DAY = 1
        private const val TIME_FORMAT = "hh:mm a"
    }
}