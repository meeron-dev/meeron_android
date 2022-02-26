package fourtune.merron.data.dto

import forutune.meeron.domain.LoginUser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginUserDto(
    @SerialName("email") val email: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("profileImageUrl") val profileImageUrl: String? = null,
    @SerialName("provider") val provider: String
) {
    companion object {
        fun from(loginUser: LoginUser): LoginUserDto =
            with(loginUser) { LoginUserDto(email, nickname, profileImageUrl, provider) }
    }
}