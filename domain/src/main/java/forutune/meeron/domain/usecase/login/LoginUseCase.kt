package forutune.meeron.domain.usecase.login

import forutune.meeron.domain.di.IoDispatcher
import forutune.meeron.domain.model.LoginUser
import forutune.meeron.domain.model.Token
import forutune.meeron.domain.model.User
import forutune.meeron.domain.repository.LoginRepository
import forutune.meeron.domain.repository.TokenRepository
import forutune.meeron.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val loginRepository: LoginRepository,
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
) {

    suspend operator fun invoke(
        getMe: suspend () -> LoginUser
    ) = withContext(dispatcher) {
        val me = getMe()

        val token = loginRepository.login(me)
        updateToken(me, token)
    }

    private suspend fun updateToken(me: LoginUser, token: Token) {
        val user = userRepository.getUser(me.email)
        if (user == null) {
            userRepository.setUser(
                User(
                    loginEmail = me.email,
                    name = me.nickname,
                    profileImageUrl = me.profileImageUrl,
                ), token
            )
        } else {
            userRepository.updateToken(user, token)
        }
        tokenRepository.saveToken(token)
    }


    suspend operator fun invoke(
        isKakaoLoginAvailable: () -> Boolean,
        kakaoLogin: suspend () -> Unit,
        kakaoLoginWithAccount: suspend () -> Unit,
        getMe: suspend () -> LoginUser
    ) = withContext(dispatcher) {
        if (isKakaoLoginAvailable()) {
            kakaoLogin()
        } else {
            kakaoLoginWithAccount()
        }
        invoke(getMe)
    }
}