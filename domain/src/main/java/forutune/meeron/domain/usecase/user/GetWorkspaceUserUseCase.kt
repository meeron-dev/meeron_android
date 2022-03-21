package forutune.meeron.domain.usecase.user

import forutune.meeron.domain.model.WorkspaceUser
import forutune.meeron.domain.repository.WorkspaceUserRepository
import javax.inject.Inject

class GetWorkspaceUserUseCase @Inject constructor(
    private val workspaceUserRepository: WorkspaceUserRepository
) {
    suspend operator fun invoke(text: String): List<WorkspaceUser> {
        return if (text.isNotEmpty()) workspaceUserRepository.getWorkspaceUsers(1, text)
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