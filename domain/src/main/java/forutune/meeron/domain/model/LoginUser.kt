package forutune.meeron.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginUser(
    val email: String,
    val nickname: String,
    val profileImageUrl: String? = null,
    val provider: String,
    val token: Token? = null
)