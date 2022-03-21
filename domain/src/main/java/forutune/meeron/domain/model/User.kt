package forutune.meeron.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("userId") val userId: Long,
    @SerialName("loginEmail") val loginEmail: String,
    @SerialName("contactEmail") val contactEmail: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("profileImageUrl") val profileImageUrl: String? = null,
    @SerialName("phone") val phone: String? = null
)
