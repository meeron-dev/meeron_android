package forutune.meeron.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MeeronError(
    @SerialName("time") val time: String,
    @SerialName("status") val status: Int,
    @SerialName("message") val errorMessage: String,
    @SerialName("code") val code: Int,
    @SerialName("errors") val errors: List<Errors> = emptyList()
) : Exception("$errorMessage / $status / $errors") {
    override fun toString(): String {
        return "$errorMessage / $status / $errors"
    }
}

@Serializable
data class Errors(
    @SerialName("field") val field: String,
    @SerialName("value") val value: String,
    @SerialName("reason") val reason: String,
)

