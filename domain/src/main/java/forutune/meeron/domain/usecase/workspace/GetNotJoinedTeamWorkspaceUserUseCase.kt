package forutune.meeron.domain.usecase.workspace

import forutune.meeron.domain.model.WorkspaceUser
import forutune.meeron.domain.repository.WorkspaceUserRepository
import javax.inject.Inject

class GetNotJoinedTeamWorkspaceUserUseCase @Inject constructor(
    private val workspaceUserRepository: WorkspaceUserRepository,
    private val getLatestWorkspaceId: GetLatestWorkspaceIdUseCase
) {
    suspend operator fun invoke(): List<WorkspaceUser> {
        val workspaceId = getLatestWorkspaceId()
        return workspaceUserRepository.getNotJoinedTeamWorkspaceUser(workspaceId)
    }
}