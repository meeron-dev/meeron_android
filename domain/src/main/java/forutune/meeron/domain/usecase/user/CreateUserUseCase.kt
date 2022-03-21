package forutune.meeron.domain.usecase.user

import forutune.meeron.domain.repository.UserRepository
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userName: String) {
        userRepository.createUserName(userName)
    }
}