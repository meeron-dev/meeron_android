package forutune.meeron.domain.usecase.workspace

import forutune.meeron.domain.model.WorkSpace
import forutune.meeron.domain.repository.WorkspaceUserRepository
import forutune.meeron.domain.usecase.me.GetMyWorkSpaceUserUseCase
import javax.inject.Inject

class ChangeWorkspaceUserUseCase @Inject constructor(
    private val workspaceUserRepository: WorkspaceUserRepository,
    private val getMyWorkSpaceUser: GetMyWorkSpaceUserUseCase
) {
    suspend operator fun invoke(workspace: WorkSpace) {
        val myWorkspaceUserId = getMyWorkSpaceUser().workspaceUserId
        workspaceUserRepository.changeWorkspaceUser(myWorkspaceUserId, workspace)
    }
}