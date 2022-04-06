package forutune.meeron.domain.usecase.login

import forutune.meeron.domain.di.IoDispatcher
import forutune.meeron.domain.repository.LoginRepository
import forutune.meeron.domain.repository.TokenRepository
import forutune.meeron.domain.repository.WorkSpaceRepository
import forutune.meeron.domain.repository.WorkspaceUserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val loginRepository: LoginRepository,
    private val tokenRepository: TokenRepository,
    private val workspaceUserRepository: WorkspaceUserRepository,
    private val workSpaceRepository: WorkSpaceRepository,
) {
    suspend operator fun invoke(kakaoLogout: suspend () -> Unit) = withContext(dispatcher) {
        kakaoLogout()
        logout()
        clearData()
    }

    private suspend fun logout() {
        val token = tokenRepository.getAccessToken().firstOrNull() ?: throw NullPointerException("accessToken is null")
        val refreshToken =
            tokenRepository.getRefreshToken().firstOrNull() ?: throw NullPointerException("refreshToken is null")
        loginRepository.logout(token, refreshToken)
    }

    private suspend fun clearData() {
        workspaceUserRepository.setCurrentWorkspaceUserId(null)
        workSpaceRepository.setCurrentWorkspaceId(null)
        tokenRepository.clearToken()
    }

    suspend operator fun invoke() = withContext(dispatcher) {
        logout()
        clearData()
    }
}