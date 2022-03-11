package forutune.meeron.domain.usecase

import forutune.meeron.domain.model.WorkspaceUser
import forutune.meeron.domain.repository.UserRepository
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(text: String): List<WorkspaceUser> {
        return if (text.isNotEmpty()) userRepository.searchUsers(1, text)
        else emptyList()
    }

    suspend operator fun invoke(vararg workspaceUserIds: Long): List<WorkspaceUser> {
        return workspaceUserIds.map {
            userRepository.searchUser(it)
        }
    }
}