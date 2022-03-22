package fourtune.merron.data.repository

import forutune.meeron.domain.model.WorkSpaceInfo
import forutune.meeron.domain.repository.WorkSpaceRepository
import fourtune.merron.data.source.remote.WorkSpaceApi
import javax.inject.Inject

class WorkSpaceRepositoryImpl @Inject constructor(
    private val workSpaceApi: WorkSpaceApi
) : WorkSpaceRepository {
    override suspend fun createWorkSpace(name: String): WorkSpaceInfo {
        val res = workSpaceApi.createWorkSpace(name)
        return WorkSpaceInfo(res.workspaceId, res.workspaceName, res.workspaceLogoUrl)
    }

}