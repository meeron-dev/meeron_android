package forutune.meeron.domain.usecase.workspace

import forutune.meeron.domain.model.WorkSpace
import forutune.meeron.domain.repository.WorkspaceUserRepository
import javax.inject.Inject

class CreateWorkspaceUserUseCase @Inject constructor(
    private val userRepository: WorkspaceUserRepository
) {
    suspend operator fun invoke(workspace: WorkSpace) {
        userRepository.createWorkspaceUser(workspace)
    }
}