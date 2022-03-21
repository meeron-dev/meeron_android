package forutune.meeron.domain.usecase.me

import forutune.meeron.domain.model.User
import forutune.meeron.domain.repository.UserRepository
import javax.inject.Inject

class GetMeUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): User {
        return userRepository.getUser()
    }
}