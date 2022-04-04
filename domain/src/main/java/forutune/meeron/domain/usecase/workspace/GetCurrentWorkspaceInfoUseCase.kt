package forutune.meeron.domain.usecase.workspace

import forutune.meeron.domain.model.WorkSpaceInfo
import javax.inject.Inject

class GetCurrentWorkspaceInfoUseCase @Inject constructor(
    private val getWorkspace: GetWorkSpaceUseCase,
    private val getLatestWorkspaceIdUseCase: GetLatestWorkspaceIdUseCase
) {
    suspend operator fun invoke(): WorkSpaceInfo {
        val workspaceId = getLatestWorkspaceIdUseCase()
        return getWorkspace(workspaceId)
    }
}