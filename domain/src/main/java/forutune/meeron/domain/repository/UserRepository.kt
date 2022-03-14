package forutune.meeron.domain.repository

import forutune.meeron.domain.model.User
import forutune.meeron.domain.model.WorkspaceUser

interface UserRepository {
    suspend fun getUsers(workspaceId: Long, nickName: String): List<WorkspaceUser>
    suspend fun getUser(workspaceUserId: Long): WorkspaceUser
    suspend fun getUsers(teamId: Long): List<WorkspaceUser>
    suspend fun getMe() : User
}