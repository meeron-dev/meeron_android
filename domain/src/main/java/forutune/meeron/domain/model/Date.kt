package forutune.meeron.domain.model

data class Date(
    val year: Int = 0,
    val month: Int = 0,
    val hourOfDay: Int = 0
) {
    override fun toString(): String {
        return "${year}/${month}/${hourOfDay}"
    }
}
