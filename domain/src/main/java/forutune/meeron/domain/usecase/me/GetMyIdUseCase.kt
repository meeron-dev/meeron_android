package forutune.meeron.domain.usecase.me

import forutune.meeron.domain.repository.UserRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GetMyIdUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): Long {
        var myId = userRepository.getUserId().firstOrNull()
        if (myId == null) {
            val me = userRepository.getUser()
            userRepository.setUserId(me.userId)
            myId = me.userId
        }
        return myId
    }
}