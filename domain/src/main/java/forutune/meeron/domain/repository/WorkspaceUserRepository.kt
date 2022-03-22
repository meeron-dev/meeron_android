package forutune.meeron.domain.repository

import forutune.meeron.domain.model.WorkSpace
import forutune.meeron.domain.model.WorkspaceUser
import kotlinx.coroutines.flow.Flow

interface WorkspaceUserRepository {
    suspend fun getWorkspaceUsers(workspaceId: Long, nickName: String): List<WorkspaceUser>
    suspend fun getWorkspaceUser(workspaceUserId: Long): WorkspaceUser
    suspend fun getWorkspaceUsers(teamId: Long): List<WorkspaceUser>

    suspend fun setCurrentWorkspaceUserId(workspaceUserId: Long)
    fun getCurrentWorkspaceUserId(): Flow<Long?>

    suspend fun createWorkspaceAdmin(workSpace: WorkSpace)
}