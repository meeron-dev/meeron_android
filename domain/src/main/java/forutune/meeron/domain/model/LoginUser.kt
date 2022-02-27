package forutune.meeron.domain.model

data class LoginUser(
    val email: String,
    val nickname: String,
    val profileImageUrl: String? = null,
    val provider: String
)