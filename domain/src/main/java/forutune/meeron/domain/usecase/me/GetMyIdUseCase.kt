package forutune.meeron.domain.usecase.me

import forutune.meeron.domain.preference.MeeronPreference
import forutune.meeron.domain.repository.UserRepository
import javax.inject.Inject

class GetMyIdUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val preference: MeeronPreference,
) {
    suspend operator fun invoke(): Long {
        var myId = preference.getMyId()
        if (myId == -1L) {
            val me = userRepository.getMe()
            preference.saveMyId(me.userId)
            myId = me.userId
        }
        return myId
    }
}