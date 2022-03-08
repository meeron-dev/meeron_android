package fourtune.merron.data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Token(
    @SerialName("type") val type: String,
    @SerialName("accessToken") val accessToken: String,
    @SerialName("refreshToken") val refreshToken: String
)
