package forutune.meeron.domain.repository

import forutune.meeron.domain.model.User
import forutune.meeron.domain.model.WorkspaceUser
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getWorkspaceUsers(workspaceId: Long, nickName: String): List<WorkspaceUser>
    suspend fun getWorkspaceUser(workspaceUserId: Long): WorkspaceUser
    suspend fun getWorkspaceUsers(teamId: Long): List<WorkspaceUser>
    suspend fun getUser(): User

    suspend fun setUserId(userId: Long)
    fun getUserId(): Flow<Long?>

    suspend fun setCurrentWorkspaceUserId(workspaceUserId: Long)
    fun getCurrentWorkspaceUserId(): Flow<Long?>
}