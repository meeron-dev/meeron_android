package forutune.meeron.domain.usecase.time

import forutune.meeron.domain.model.Date
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

sealed class DateFormat(val format: String) {
    object YYMMDD : DateFormat("yy/MM/dd")
    object YYYYMMDD : DateFormat("yyyy/MM/dd")
    object SimpleString : DateFormat("yyyy년 MM월 dd일")
}

sealed interface DateResult {
    class YYMMDD(val date: Date) : DateResult
    class YYYYMMDD(val date: Date) : DateResult
    class SimpleString(val string: String) : DateResult
}

class GetCurrentDayUseCase @Inject constructor(
    private val calendar: Calendar
) {
    operator fun invoke(format: DateFormat.YYMMDD): DateResult.YYMMDD {
        val dateSection = getFormattedDate(format).split("/")
        return DateResult.YYMMDD(Date(dateSection[YEAR], dateSection[MONTH], dateSection[HOUR_OF_DAY]))
    }

    operator fun invoke(format: DateFormat.SimpleString): DateResult.SimpleString {
        return DateResult.SimpleString(getFormattedDate(format))
    }

    operator fun invoke(format: DateFormat.YYYYMMDD): DateResult.YYYYMMDD {
        val dateSection = getFormattedDate(format).split("/")
        return DateResult.YYYYMMDD(Date(dateSection[YEAR], dateSection[MONTH], dateSection[HOUR_OF_DAY]))
    }

    private fun getFormattedDate(format: DateFormat): String =
        SimpleDateFormat(format.format, Locale.US).format(calendar.time)

    companion object {
        private const val YEAR = 0
        private const val MONTH = 1
        private const val HOUR_OF_DAY = 2
    }
}