package forutune.meeron.domain.usecase.workspace

import forutune.meeron.domain.model.WorkSpaceInfo
import forutune.meeron.domain.repository.WorkSpaceRepository
import javax.inject.Inject

class GetWorkSpaceUseCase @Inject constructor(
    private val workSpaceRepository: WorkSpaceRepository
) {
    suspend operator fun invoke(workspaceId: Long): WorkSpaceInfo {
        return workSpaceRepository.getWorkspace(workspaceId)
    }
}