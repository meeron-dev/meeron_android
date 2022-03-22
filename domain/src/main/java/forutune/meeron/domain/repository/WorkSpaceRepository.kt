package forutune.meeron.domain.repository

import forutune.meeron.domain.model.WorkSpaceInfo

interface WorkSpaceRepository {
    suspend fun createWorkSpace(name: String): WorkSpaceInfo
}