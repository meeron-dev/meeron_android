package fourtune.merron.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import forutune.meeron.domain.model.WorkSpaceInfo
import forutune.meeron.domain.repository.WorkSpaceRepository
import fourtune.merron.data.source.local.preference.DataStoreKeys
import fourtune.merron.data.source.remote.WorkSpaceApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WorkSpaceRepositoryImpl @Inject constructor(
    private val workSpaceApi: WorkSpaceApi,
    private val dataStore: DataStore<Preferences>
) : WorkSpaceRepository {
    override suspend fun createWorkSpace(name: String): WorkSpaceInfo {
        val res = workSpaceApi.createWorkSpace(name)
        return WorkSpaceInfo(res.workspaceId, res.workspaceName, res.workspaceLogoUrl)
    }

    override suspend fun getUserWorkspace(userId: Long): List<WorkSpaceInfo> {
        return workSpaceApi.getUserWorkspaces(userId).myWorkspaces.map {
            WorkSpaceInfo(it.workspaceId, it.workspaceName, it.workspaceLogoUrl)
        }
    }

    override suspend fun getWorkspace(workspaceId: Long): WorkSpaceInfo {
        val res = workSpaceApi.getWorkSpace(workspaceId)
        return WorkSpaceInfo(res.workspaceId, res.workspaceName, res.workspaceLogoUrl)
    }

    override suspend fun getCurrentWorkspaceId(): Long? {
        return dataStore.data.map {
            it[DataStoreKeys.Workspace.id]
        }.firstOrNull()

    }

    override suspend fun setCurrentWorkspaceId(workspaceId: Long) {
        dataStore.edit {
            it[DataStoreKeys.Workspace.id] = workspaceId
        }
    }

}