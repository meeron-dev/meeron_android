package forutune.meeron.domain.usecase

import forutune.meeron.domain.repository.TokenRepository
import forutune.meeron.domain.repository.WorkSpaceRepository
import forutune.meeron.domain.repository.WorkspaceUserRepository
import javax.inject.Inject

class ClearDataUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val workspaceUserRepository: WorkspaceUserRepository,
    private val workSpaceRepository: WorkSpaceRepository,
) {
    suspend operator fun invoke() {
        workspaceUserRepository.setCurrentWorkspaceUserId(null)
        workSpaceRepository.setCurrentWorkspaceId(null)
        tokenRepository.clearToken()
    }
}