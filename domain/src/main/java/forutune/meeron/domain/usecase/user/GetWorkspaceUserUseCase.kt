package forutune.meeron.domain.usecase.user

import forutune.meeron.domain.model.WorkspaceUser
import forutune.meeron.domain.repository.WorkspaceUserRepository
import forutune.meeron.domain.usecase.workspace.GetLatestWorkspaceIdUseCase
import javax.inject.Inject

class GetWorkspaceUserUseCase @Inject constructor(
    private val workspaceUserRepository: WorkspaceUserRepository,
    private val getLatestWorkspaceId: GetLatestWorkspaceIdUseCase
) {
    suspend operator fun invoke(text: String): List<WorkspaceUser> {
        val workspaceId = getLatestWorkspaceId()
        return if (text.isNotEmpty()) workspaceUserRepository.getWorkspaceUsers(workspaceId, text)
        else emptyList()
    }

    suspend operator fun invoke(vararg workspaceUserIds: Long): List<WorkspaceUser> {
        return workspaceUserIds.map {
            workspaceUserRepository.getWorkspaceUser(it)
        }
    }

    suspend operator fun invoke(teamId: Long): List<WorkspaceUser> {
        return workspaceUserRepository.getWorkspaceUsers(teamId)
    }
}