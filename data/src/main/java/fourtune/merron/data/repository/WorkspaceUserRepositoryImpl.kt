package fourtune.merron.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import forutune.meeron.domain.model.WorkspaceUser
import forutune.meeron.domain.repository.WorkspaceUserRepository
import fourtune.merron.data.source.local.preference.DataStoreKeys
import fourtune.merron.data.source.remote.WorkspaceUserApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WorkspaceUserRepositoryImpl @Inject constructor(
    private val workspaceUserApi: WorkspaceUserApi,
    private val dataStore: DataStore<Preferences>
) : WorkspaceUserRepository {
    override suspend fun getWorkspaceUsers(workspaceId: Long, nickName: String): List<WorkspaceUser> {
        return workspaceUserApi.getUsers(workspaceId, nickName).workspaceUsers
    }

    override suspend fun getWorkspaceUsers(teamId: Long): List<WorkspaceUser> {
        return workspaceUserApi.getTeamUser(teamId).workspaceUsers
    }

    override suspend fun getWorkspaceUser(workspaceUserId: Long): WorkspaceUser {
        return workspaceUserApi.getWorkspaceUser(workspaceUserId)
    }


    override suspend fun setCurrentWorkspaceUserId(workspaceUserId: Long) {
        dataStore.edit {
            it[DataStoreKeys.WorkspaceUser.id] = workspaceUserId
        }
    }

    override fun getCurrentWorkspaceUserId(): Flow<Long?> {
        return dataStore.data.map {
            it[DataStoreKeys.WorkspaceUser.id]
        }
    }
}