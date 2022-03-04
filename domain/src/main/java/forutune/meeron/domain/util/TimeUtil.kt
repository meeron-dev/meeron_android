package forutune.meeron.domain.util

import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {
    fun getTimeSection(time: Date): List<String> {
        val formattedTime: String = SimpleDateFormat(TIME_FORMAT, Locale.US).format(time)
        return formattedTime.split(" ")
    }

     const val TIME = 0
     const val HOUR_OF_DAY = 1
     private const val TIME_FORMAT = "hh:mm a"
}