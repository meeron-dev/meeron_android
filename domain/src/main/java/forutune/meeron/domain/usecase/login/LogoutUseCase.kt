package forutune.meeron.domain.usecase.login

import forutune.meeron.domain.di.IoDispatcher
import forutune.meeron.domain.repository.LoginRepository
import forutune.meeron.domain.repository.TokenRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val loginRepository: LoginRepository,
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(kakaoLogout: suspend () -> Unit) = withContext(dispatcher) {
        kakaoLogout()
        loginRepository.logout()
        tokenRepository.clearToken()
    }
}