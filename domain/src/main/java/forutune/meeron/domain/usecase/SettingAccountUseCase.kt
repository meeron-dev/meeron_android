package forutune.meeron.domain.usecase

import forutune.meeron.domain.repository.UserRepository
import forutune.meeron.domain.repository.WorkSpaceRepository
import forutune.meeron.domain.repository.WorkspaceUserRepository
import forutune.meeron.domain.usecase.me.GetMeUseCase
import forutune.meeron.domain.usecase.workspace.GetLatestWorkspaceIdUseCase
import javax.inject.Inject

class SettingAccountUseCase @Inject constructor(
    private val getLatestWorkspaceId: GetLatestWorkspaceIdUseCase,
    private val workSpaceRepository: WorkSpaceRepository,
    private val workspaceUserRepository: WorkspaceUserRepository,
    private val getMe: GetMeUseCase,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(workspaceId: Long? = null) {
        val workspaceId = workspaceId ?: getLatestWorkspaceId()
        workSpaceRepository.setCurrentWorkspaceId(workspaceId)

        val me = getMe()
        userRepository.setUserId(me.userId)

        val myWorkspaceUsers = workspaceUserRepository.getMyWorkspaceUsers(me.userId)
        println("myWorkspaceUsers : $myWorkspaceUsers")
        val currentWorkspaceUser = myWorkspaceUsers.find { it.workspaceId == workspaceId }
            ?: throw Exception("not found workspaceUser")
        workspaceUserRepository.setCurrentWorkspaceUserId(currentWorkspaceUser.workspaceUserId)
    }
}