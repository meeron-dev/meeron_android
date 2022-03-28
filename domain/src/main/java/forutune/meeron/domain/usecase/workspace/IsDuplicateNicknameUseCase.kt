package forutune.meeron.domain.usecase.workspace

import forutune.meeron.domain.repository.WorkspaceUserRepository
import javax.inject.Inject

class IsDuplicateNicknameUseCase @Inject constructor(
    private val workspaceUserRepository: WorkspaceUserRepository,
    private val getLatestWorkspaceId: GetLatestWorkspaceIdUseCase
) {
    suspend operator fun invoke(nickName: String): Boolean {
        val workspaceId = getLatestWorkspaceId()
        return workspaceUserRepository.isDuplicateWorkspaceUser(workspaceId, nickName)
    }
}