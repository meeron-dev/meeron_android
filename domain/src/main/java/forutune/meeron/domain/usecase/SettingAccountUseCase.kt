package forutune.meeron.domain.usecase

import forutune.meeron.domain.repository.WorkSpaceRepository
import forutune.meeron.domain.repository.WorkspaceUserRepository
import forutune.meeron.domain.usecase.me.GetMeUseCase
import forutune.meeron.domain.usecase.workspace.GetLatestWorkspaceIdUseCase
import javax.inject.Inject

class SettingAccountUseCase @Inject constructor(
    private val getLatestWorkspaceId: GetLatestWorkspaceIdUseCase,
    private val workSpaceRepository: WorkSpaceRepository,
    private val workspaceUserRepository: WorkspaceUserRepository,
    private val getMe: GetMeUseCase
) {
    suspend operator fun invoke() {
        val workspaceId = getLatestWorkspaceId()
        workSpaceRepository.setCurrentWorkspaceId(workspaceId)

        val me = getMe()
        val myWorkspaceUsers = workspaceUserRepository.getMyWorkspaceUsers(me.userId)
        val currentWorkspaceUser = myWorkspaceUsers.find { it.workspaceId == workspaceId }
            ?: throw Exception("not fount workspaceUser")
        workspaceUserRepository.setCurrentWorkspaceUserId(currentWorkspaceUser.workspaceUserId)
    }
}