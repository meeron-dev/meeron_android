package forutune.meeron.domain.model

data class Time(
    val time: String = "",
    val hourOfDay: String = ""
) {
    override fun toString(): String {
        return "$time$hourOfDay"
    }
}