package fourtune.merron.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import forutune.meeron.domain.model.WorkSpace
import forutune.meeron.domain.model.WorkspaceUser
import forutune.meeron.domain.repository.WorkspaceUserRepository
import fourtune.merron.data.model.dto.request.WorkSpaceRequest
import fourtune.merron.data.source.local.preference.DataStoreKeys
import fourtune.merron.data.source.remote.WorkspaceUserApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class WorkspaceUserRepositoryImpl @Inject constructor(
    private val workspaceUserApi: WorkspaceUserApi,
    private val dataStore: DataStore<Preferences>,
    private val fileProvider: forutune.meeron.domain.provider.FileProvider
) : WorkspaceUserRepository {

    override suspend fun getMyWorkspaceUsers(userId: Long): List<WorkspaceUser> {
        return workspaceUserApi.getWorkspaceUsers(userId).myWorkspaceUsers
    }

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
        Timber.tag("🔥setWorkspaceUserId").d("$workspaceUserId")
        dataStore.edit {
            it[DataStoreKeys.Workspace.userId] = workspaceUserId
        }
    }

    override suspend fun getCurrentWorkspaceUserId(): Long? {
        return dataStore.data.map {
            it[DataStoreKeys.Workspace.userId]
        }.firstOrNull().also { Timber.tag("🔥getWorkspaceUserId").d("$it") }
    }

    override suspend fun createWorkspaceAdmin(workSpace: WorkSpace) {
        val files = createFileData(workSpace)
        val requestBody = createWorkspaceRequestBody(workSpace)

        workspaceUserApi.createWorkSpaceAdmin(
            request = MultipartBody.Part.createFormData(name = "request", body = requestBody, filename = "request"),
            files = files
        )
    }

    override suspend fun createWorkspaceUser(workSpace: WorkSpace) {
        val files = createFileData(workSpace)
        val requestBody = createWorkspaceRequestBody(workSpace)
        workspaceUserApi.createWorkSpaceUser(
            request = MultipartBody.Part.createFormData(name = "request", body = requestBody, filename = "request"),
            files = files
        )
    }

    private fun createWorkspaceRequestBody(workSpace: WorkSpace) = Json.encodeToString(
        WorkSpaceRequest(
            workspaceId = workSpace.workspaceId,
            nickname = workSpace.nickname,
            position = workSpace.position,
            email = workSpace.email,
            phone = workSpace.phone
        )
    ).toRequestBody("application/json".toMediaType())

    private fun createFileData(workSpace: WorkSpace) = kotlin.runCatching {
        val fileName = fileProvider.getFileName(workSpace.image)
        val pathName = fileProvider.getPath(workSpace.image)
        if (pathName != null) MultipartBody.Part.createFormData(
            name = "files",
            filename = fileName,
            body = File(pathName).asRequestBody("image/*".toMediaType())
        ) else null
    }.onFailure { Timber.tag("🔥zero:fileCreation").e("$it") }.getOrNull()
}