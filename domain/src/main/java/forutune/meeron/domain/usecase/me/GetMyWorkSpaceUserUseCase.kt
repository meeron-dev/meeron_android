package forutune.meeron.domain.usecase.me

import forutune.meeron.domain.model.WorkspaceUser
import forutune.meeron.domain.repository.WorkspaceUserRepository
import javax.inject.Inject

class GetMyWorkSpaceUserUseCase @Inject constructor(
    private val workspaceUserRepository: WorkspaceUserRepository
) {
    suspend operator fun invoke(): WorkspaceUser {
        val workspaceUserId = workspaceUserRepository.getCurrentWorkspaceUserId()
        checkNotNull(workspaceUserId)
        return workspaceUserRepository.getWorkspaceUser(workspaceUserId)
    }
}