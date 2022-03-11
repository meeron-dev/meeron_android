package forutune.meeron.domain.repository

import forutune.meeron.domain.model.WorkspaceUser

interface UserRepository {
    suspend fun searchUsers(workspaceId: Long, nickName: String): List<WorkspaceUser>
    suspend fun searchUser(workspaceUserId: Long): WorkspaceUser
}