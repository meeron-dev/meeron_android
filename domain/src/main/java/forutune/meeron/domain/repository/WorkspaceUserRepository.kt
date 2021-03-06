package forutune.meeron.domain.repository

import forutune.meeron.domain.model.WorkSpace
import forutune.meeron.domain.model.WorkspaceUser
import java.io.File

interface WorkspaceUserRepository {
    suspend fun getMyWorkspaceUsers(userId: Long): List<WorkspaceUser>
    suspend fun getWorkspaceUsers(workspaceId: Long, nickName: String): List<WorkspaceUser>
    suspend fun getWorkspaceUser(workspaceUserId: Long): WorkspaceUser
    suspend fun getWorkspaceUsers(teamId: Long): List<WorkspaceUser>

    suspend fun setCurrentWorkspaceUserId(workspaceUserId: Long?)
    suspend fun getCurrentWorkspaceUserId(): Long?

    suspend fun createWorkspaceAdmin(workSpace: WorkSpace): WorkspaceUser
    suspend fun createWorkspaceUser(workSpace: WorkSpace)

    suspend fun isDuplicateWorkspaceUser(workspaceId: Long, nickName: String): Boolean

    suspend fun getNotJoinedTeamWorkspaceUser(workspaceId: Long): List<WorkspaceUser>
    suspend fun changeWorkspaceUser(workspaceUserId: Long, workspace: WorkSpace)

    suspend fun getUserProfile(workspaceUserId: Long, onLoadComplete: (File) -> Unit)
}