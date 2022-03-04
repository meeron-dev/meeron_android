package forutune.meeron.domain.usecase.time

import forutune.meeron.domain.model.Time
import forutune.meeron.domain.util.TimeUtil
import forutune.meeron.domain.util.TimeUtil.HOUR_OF_DAY
import forutune.meeron.domain.util.TimeUtil.TIME
import java.util.*
import javax.inject.Inject

class GetCurrentTimeUseCase @Inject constructor(
    private val calendar: Calendar
) {
    operator fun invoke(onTime: Boolean): Time {
        val time: Date = calendar.apply {
            if (onTime) set(Calendar.MINUTE, 0)
        }.time
        val timeSection = TimeUtil.getTimeSection(time)
        return Time(time = timeSection[TIME], hourOfDay = timeSection[HOUR_OF_DAY])
    }

}