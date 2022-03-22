package forutune.meeron.domain.usecase.workspace

import forutune.meeron.domain.model.WorkSpaceInfo
import forutune.meeron.domain.repository.UserRepository
import forutune.meeron.domain.repository.WorkSpaceRepository
import javax.inject.Inject

class GetUserWorkspacesUseCase @Inject constructor(
    private val workSpaceRepository: WorkSpaceRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): List<WorkSpaceInfo> {
        val userId = userRepository.getUserId()
        return workSpaceRepository.getUserWorkspace(userId)
    }
}