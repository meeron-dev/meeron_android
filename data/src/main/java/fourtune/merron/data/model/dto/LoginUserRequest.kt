package fourtune.merron.data.model.dto

import forutune.meeron.domain.model.LoginUser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginUserRequest(
    @SerialName("email") val email: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("profileImageUrl") val profileImageUrl: String? = null,
    @SerialName("provider") val provider: String
) {
    companion object {
        fun from(loginUser: LoginUser): LoginUserRequest =
            with(loginUser) { LoginUserRequest(email, nickname, profileImageUrl, provider) }
    }
}