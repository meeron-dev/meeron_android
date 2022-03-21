package fourtune.merron.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import forutune.meeron.domain.model.User
import forutune.meeron.domain.model.WorkspaceUser
import forutune.meeron.domain.repository.UserRepository
import fourtune.merron.data.source.local.preference.DataStoreKeys
import fourtune.merron.data.source.remote.UserApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val dataStore: DataStore<Preferences>
) : UserRepository {
    override suspend fun getWorkspaceUsers(workspaceId: Long, nickName: String): List<WorkspaceUser> {
        return userApi.getUsers(workspaceId, nickName).workspaceUsers
    }

    override suspend fun getWorkspaceUsers(teamId: Long): List<WorkspaceUser> {
        return userApi.getTeamUser(teamId).workspaceUsers
    }

    override suspend fun getWorkspaceUser(workspaceUserId: Long): WorkspaceUser {
        return userApi.getWorkspaceUser(workspaceUserId)
    }

    override suspend fun getUser(): User {
        return userApi.getMe()
    }

    override suspend fun setUserId(userId: Long) {
        dataStore.edit {
            it[DataStoreKeys.User.id] = userId
        }
    }

    override fun getUserId(): Flow<Long?> {
        return dataStore.data.map { it[DataStoreKeys.User.id] }
    }

    override suspend fun setCurrentWorkspaceUserId(workspaceUserId: Long) {
        dataStore.edit {
            it[DataStoreKeys.User.workSpaceUserId] = workspaceUserId
        }
    }

    override fun getCurrentWorkspaceUserId(): Flow<Long?> {
        return dataStore.data.map {
            it[DataStoreKeys.User.workSpaceUserId]
        }
    }
}