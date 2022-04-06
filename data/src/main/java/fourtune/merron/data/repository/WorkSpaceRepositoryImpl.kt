package fourtune.merron.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import forutune.meeron.domain.model.WorkSpaceInfo
import forutune.meeron.domain.repository.WorkSpaceRepository
import fourtune.merron.data.source.local.preference.DataStoreKeys
import fourtune.merron.data.source.remote.WorkSpaceApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import timber.log.Timber
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

    override suspend fun getCurrentWorkspaceId(): Long {
        val workspaceId = dataStore.data.mapNotNull {
            it[DataStoreKeys.Workspace.id]
        }.first()
        return workspaceId.also { Timber.tag("🔥getCurrentWorkspaceId").d("$it") }

    }

    override suspend fun setCurrentWorkspaceId(workspaceId: Long?) {
        Timber.tag("🔥setCurrentWorkspaceId").d("$workspaceId")
        dataStore.edit {
            if (workspaceId == null) {
                it.remove(DataStoreKeys.Workspace.id)
            } else {
                it[DataStoreKeys.Workspace.id] = workspaceId
            }
        }
    }

}