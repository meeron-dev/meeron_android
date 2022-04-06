package forutune.meeron.domain.usecase.workspace

import forutune.meeron.domain.model.WorkSpaceInfo
import forutune.meeron.domain.repository.WorkSpaceRepository
import forutune.meeron.domain.repository.WorkspaceUserRepository
import forutune.meeron.domain.usecase.me.GetMeUseCase
import javax.inject.Inject

class SetCurrentWorkspaceInfoUseCase @Inject constructor(
    private val workSpaceRepository: WorkSpaceRepository,
    private val workspaceUserRepository: WorkspaceUserRepository,
    private val getMe: GetMeUseCase
) {
    @Deprecated("멀티 스페이스로 된다면 변경해야 함")
    suspend operator fun invoke(workspaces: List<WorkSpaceInfo>) {
        val workspaceId = workspaces.first().workSpaceId
        val me = getMe()
        workSpaceRepository.setCurrentWorkspaceId(workspaceId)
        val workspaceUser =
            workspaceUserRepository.getMyWorkspaceUsers(me.userId).first { it.workspaceId == workspaceId }
        workspaceUserRepository.setCurrentWorkspaceUserId(workspaceUser.workspaceUserId)
    }
}