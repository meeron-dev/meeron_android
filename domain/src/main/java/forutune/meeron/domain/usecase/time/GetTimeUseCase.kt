package forutune.meeron.domain.usecase.time

import forutune.meeron.domain.model.Time
import forutune.meeron.domain.util.TimeUtil
import forutune.meeron.domain.util.TimeUtil.HOUR_OF_DAY
import forutune.meeron.domain.util.TimeUtil.TIME
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
        val timeSection = TimeUtil.getTimeSection(time)
        return Time(time = timeSection[TIME], hourOfDay = timeSection[HOUR_OF_DAY])
    }


}