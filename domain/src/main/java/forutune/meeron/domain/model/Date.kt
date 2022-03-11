package forutune.meeron.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Date(
    val year: Int = 0,
    val month: Int = 0,
    val hourOfDay: Int = 0
) {
    fun formattedString(): String {
        return "${year}/${month}/${hourOfDay}"
    }

    fun displayString(): String {
        return "${year}년 ${month}월 ${hourOfDay}일"
    }
}
