package fourtune.merron.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import dagger.hilt.android.qualifiers.ApplicationContext
import forutune.meeron.domain.model.WorkSpace
import forutune.meeron.domain.model.WorkspaceUser
import forutune.meeron.domain.provider.FileProvider
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
    @ApplicationContext private val context: Context,
    private val workspaceUserApi: WorkspaceUserApi,
    private val dataStore: DataStore<Preferences>,
    private val fileProvider: FileProvider,
    private val transferUtility: TransferUtility
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

    override suspend fun getUserProfile(workspaceUserId: Long, onLoadComplete: (File) -> Unit) {
        val workspaceUser = getWorkspaceUser(workspaceUserId)
        val key = workspaceUser.profileImageUrl.split("/").last()
        val file = File(context.filesDir, key)
        if (file.isDirectory) {
            onLoadComplete(file)
        } else {
            transferUtility
                .download("files/$key", file, object : TransferListener {
                    override fun onStateChanged(id: Int, state: TransferState?) {
                        Timber.tag("🔥zero:onStateChanged").w("$state / $file")
                        when (state) {
                            TransferState.IN_PROGRESS -> ""
                            TransferState.COMPLETED -> onLoadComplete(file)
                        }
                    }

                    override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                        Timber.tag("🔥zero:onProgress").d("$bytesCurrent")
                    }

                    override fun onError(id: Int, ex: Exception?) {
                        Timber.tag("🔥zero:onError").e("$ex")
                    }
                })
        }

    }

    override suspend fun getWorkspaceUser(workspaceUserId: Long): WorkspaceUser {
        return workspaceUserApi.getWorkspaceUser(workspaceUserId)
    }


    override suspend fun setCurrentWorkspaceUserId(workspaceUserId: Long?) {
        Timber.tag("🔥setWorkspaceUserId").d("$workspaceUserId")
        dataStore.edit {
            if (workspaceUserId == null) {
                it.remove(DataStoreKeys.Workspace.userId)
            } else {
                it[DataStoreKeys.Workspace.userId] = workspaceUserId
            }
        }
    }

    override suspend fun getCurrentWorkspaceUserId(): Long? {
        return dataStore.data.map {
            it[DataStoreKeys.Workspace.userId]
        }.firstOrNull().also { Timber.tag("🔥getWorkspaceUserId").d("$it") }
    }

    override suspend fun createWorkspaceAdmin(workSpace: WorkSpace): WorkspaceUser {
        val files = createFileData(workSpace)
        val requestBody = createWorkspaceRequestBody(workSpace)

        return workspaceUserApi.createWorkSpaceAdmin(
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

    override suspend fun isDuplicateWorkspaceUser(workspaceId: Long, nickName: String): Boolean {
        return workspaceUserApi.isDuplicateWorkspaceUser(workspaceId, nickName).duplicate
    }

    override suspend fun getNotJoinedTeamWorkspaceUser(workspaceId: Long): List<WorkspaceUser> {
        return workspaceUserApi.getNotJoinedTeamWorkspaceUser(workspaceId).workspaceUsers
    }

    override suspend fun changeWorkspaceUser(workspaceUserId: Long, workspace: WorkSpace) {
        val files = createFileData(workspace)
        val requestBody = createWorkspaceRequestBody(workspace)
        workspaceUserApi.changeWorkspaceUser(
            workspaceUserId = workspaceUserId,
            files = files,
            request = MultipartBody.Part.createFormData(name = "request", body = requestBody, filename = "request")
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