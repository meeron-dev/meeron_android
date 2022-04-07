package forutune.meeron.domain.usecase.workspace

import forutune.meeron.domain.model.WorkSpace
import forutune.meeron.domain.model.WorkSpaceInfo
import forutune.meeron.domain.repository.WorkspaceUserRepository
import javax.inject.Inject

class CreateWorkspaceUserUseCase @Inject constructor(
    private val userRepository: WorkspaceUserRepository,
    private val setCurrentWorkspaceInfoUseCase: SetCurrentWorkspaceInfoUseCase
) {
    suspend operator fun invoke(workspace: WorkSpace) {
        userRepository.createWorkspaceUser(workspace)
        setCurrentWorkspaceInfoUseCase(listOf(WorkSpaceInfo(workspace.workspaceId)))
    }
}