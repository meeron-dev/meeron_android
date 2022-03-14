package forutune.meeron.domain.usecase

import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.preference.MeeronPreference
import forutune.meeron.domain.repository.MeetingRepository
import forutune.meeron.domain.repository.UserRepository
import javax.inject.Inject

class GetMeetingUseCase @Inject constructor(
    private val meetingRepository: MeetingRepository,
    private val userRepository: UserRepository,
    private val preference: MeeronPreference
) {
    suspend operator fun invoke(): List<Meeting> {
        var myid = preference.getMyId()
        if (myid == -1L) {
            val me = userRepository.getMe()
            preference.saveMyId(me.userId)
            myid = me.userId
        }

        return meetingRepository.getTodayMeetings(1, myid)
    }
}