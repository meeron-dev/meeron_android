package forutune.meeron.domain.usecase

import forutune.meeron.domain.LoginUser
import forutune.meeron.domain.repository.LoginRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    suspend operator fun invoke(
        getMe: suspend () -> LoginUser
    ): Boolean = runCatching { loginRepository.login(getMe()) }.isSuccess


    suspend operator fun invoke(
        isKakaoLoginAvailable: () -> Boolean,
        kakaoLogin: suspend () -> Unit,
        kakaoLoginWithAccount: suspend () -> Unit,
        getMe: suspend () -> LoginUser
    ): Boolean {
        if (isKakaoLoginAvailable()) {
            kakaoLogin()
        } else {
            kakaoLoginWithAccount()
        }
        return invoke(getMe)
    }
}