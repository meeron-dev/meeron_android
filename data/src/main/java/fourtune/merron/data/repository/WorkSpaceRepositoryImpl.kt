package fourtune.merron.data.repository

import forutune.meeron.domain.model.WorkSpaceInfo
import forutune.meeron.domain.repository.WorkSpaceRepository
import fourtune.merron.data.source.remote.WorkSpaceApi
import javax.inject.Inject

class WorkSpaceRepositoryImpl @Inject constructor(
    private val workSpaceApi: WorkSpaceApi
) : WorkSpaceRepository {
    override suspend fun createWorkSpace(name: String): WorkSpaceInfo {
        val res = workSpaceApi.createWorkSpace(name)
        return WorkSpaceInfo(res.workspaceId, res.workspaceName, res.workspaceLogoUrl)
    }

    override suspend fun getUserWorkspace(userId: Long): List<WorkSpaceInfo> {
        return workSpaceApi.getUserWorkspaces(userId).myWorkspaces.map {
            WorkSpaceInfo(it.workspaceId, it.workspaceName, it.workspaceLogoUrl)
        }
    }

    override suspend fun getWorkspace(workspaceId: Long): WorkSpaceInfo {
        val res = workSpaceApi.getWorkSpace(workspaceId)
        return WorkSpaceInfo(res.workspaceId, res.workspaceName, res.workspaceLogoUrl)
    }

}