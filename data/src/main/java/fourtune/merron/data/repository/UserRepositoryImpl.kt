package fourtune.merron.data.repository

import forutune.meeron.domain.model.WorkspaceUser
import forutune.meeron.domain.repository.UserRepository
import fourtune.merron.data.source.remote.UserApi
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi
) : UserRepository {
    override suspend fun searchUsers(workspaceId: Long, nickName: String): List<WorkspaceUser> {
        return userApi.searchUsers(workspaceId, nickName).workspaceUsers
    }

    override suspend fun searchUser(workspaceUserId: Long): WorkspaceUser {
        return userApi.getWorkspaceUser(workspaceUserId)
    }


}