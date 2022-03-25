package forutune.meeron.domain.repository

import forutune.meeron.domain.model.WorkSpaceInfo

interface WorkSpaceRepository {
    suspend fun createWorkSpace(name: String): WorkSpaceInfo
    suspend fun getUserWorkspace(userId: Long): List<WorkSpaceInfo>
    suspend fun getWorkspace(workspaceId: Long): WorkSpaceInfo
    suspend fun getCurrentWorkspaceId(): Long?
    suspend fun setCurrentWorkspaceId(workspaceId: Long)
}