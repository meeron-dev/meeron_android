package forutune.meeron.domain.usecase.time

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class GetCurrentDayUseCase @Inject constructor(
    private val calendar: Calendar
) {
    operator fun invoke(): String {
        return SimpleDateFormat(TIME_FORMAT, Locale.US).format(calendar.time)
    }

    companion object {
        private const val TIME_FORMAT = "yyyy년 MM월 dd일"
    }
}