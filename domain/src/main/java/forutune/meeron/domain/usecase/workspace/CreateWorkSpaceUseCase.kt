package forutune.meeron.domain.usecase.workspace

import forutune.meeron.domain.model.WorkSpace
import forutune.meeron.domain.repository.TeamRepository
import forutune.meeron.domain.repository.WorkSpaceRepository
import forutune.meeron.domain.repository.WorkspaceUserRepository
import javax.inject.Inject

class CreateWorkSpaceUseCase @Inject constructor(
    private val workSpaceRepository: WorkSpaceRepository,
    private val workspaceUserRepository: WorkspaceUserRepository,
    private val teamRepository: TeamRepository
) {
    suspend operator fun invoke(workSpace: WorkSpace, teamName: String) {
        val workspaceInfo = workSpaceRepository.createWorkSpace(workSpace.workspaceName)
        workspaceUserRepository.createWorkspaceAdmin(workSpace.copy(workspaceId = workspaceInfo.workSpaceId))
        teamRepository.createTeam(workspaceInfo.workSpaceId, teamName)
    }
}