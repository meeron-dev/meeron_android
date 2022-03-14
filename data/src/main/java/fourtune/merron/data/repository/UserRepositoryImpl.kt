package fourtune.merron.data.repository

import forutune.meeron.domain.model.User
import forutune.meeron.domain.model.WorkspaceUser
import forutune.meeron.domain.repository.UserRepository
import fourtune.merron.data.source.remote.UserApi
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi
) : UserRepository {
    override suspend fun getUsers(workspaceId: Long, nickName: String): List<WorkspaceUser> {
        return userApi.getUsers(workspaceId, nickName).workspaceUsers
    }

    override suspend fun getUsers(teamId: Long): List<WorkspaceUser> {
        return userApi.getTeamUser(teamId).workspaceUsers
    }

    override suspend fun getUser(workspaceUserId: Long): WorkspaceUser {
        return userApi.getWorkspaceUser(workspaceUserId)
    }

    override suspend fun getMe(): User {
        return userApi.getMe()
    }


}