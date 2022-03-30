package forutune.meeron.domain.usecase.workspace

import forutune.meeron.domain.model.WorkSpace
import forutune.meeron.domain.model.WorkSpaceInfo
import forutune.meeron.domain.repository.TeamRepository
import forutune.meeron.domain.repository.WorkSpaceRepository
import forutune.meeron.domain.repository.WorkspaceUserRepository
import javax.inject.Inject

class CreateWorkSpaceUseCase @Inject constructor(
    private val workSpaceRepository: WorkSpaceRepository,
    private val workspaceUserRepository: WorkspaceUserRepository,
    private val teamRepository: TeamRepository
) {
    suspend operator fun invoke(workSpace: WorkSpace, teamName: String): Long {
        val workspaceId = createWorkspace(workSpace).workSpaceId
        createWorkspaceAdmin(workSpace, workspaceId)
        teamRepository.createTeam(workspaceId, teamName)
        return workspaceId
    }

    private suspend fun createWorkspace(workSpace: WorkSpace): WorkSpaceInfo {
        val workspaceInfo = workSpaceRepository.createWorkSpace(workSpace.workspaceName)
        workSpaceRepository.setCurrentWorkspaceId(workspaceInfo.workSpaceId)
        return workspaceInfo
    }

    private suspend fun createWorkspaceAdmin(
        workSpace: WorkSpace,
        workspaceId: Long
    ) {
        val workspaceAdmin =
            workspaceUserRepository.createWorkspaceAdmin(workSpace.copy(workspaceId = workspaceId))
        workspaceUserRepository.setCurrentWorkspaceUserId(workspaceAdmin.workspaceUserId)
    }
}