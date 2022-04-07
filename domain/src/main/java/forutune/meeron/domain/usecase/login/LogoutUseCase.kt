package forutune.meeron.domain.usecase.login

import forutune.meeron.domain.di.IoDispatcher
import forutune.meeron.domain.repository.LoginRepository
import forutune.meeron.domain.repository.TokenRepository
import forutune.meeron.domain.usecase.ClearDataUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val loginRepository: LoginRepository,
    private val tokenRepository: TokenRepository,
    private val clearData: ClearDataUseCase
) {
    suspend operator fun invoke(kakaoLogout: suspend () -> Unit) = withContext(dispatcher) {
        kakaoLogout()
        logout()
        clearData.invoke()
    }

    private suspend fun logout() {
        val token = tokenRepository.getAccessToken().firstOrNull() ?: throw NullPointerException("accessToken is null")
        val refreshToken =
            tokenRepository.getRefreshToken().firstOrNull() ?: throw NullPointerException("refreshToken is null")
        loginRepository.logout(token, refreshToken)
    }


    suspend operator fun invoke() = withContext(dispatcher) {
        logout()
        clearData.invoke()
    }
}