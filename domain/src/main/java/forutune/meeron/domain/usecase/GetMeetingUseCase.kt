package forutune.meeron.domain.usecase

import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.repository.MeetingRepository
import forutune.meeron.domain.usecase.me.GetMyIdUseCase
import javax.inject.Inject

class GetMeetingUseCase @Inject constructor(
    private val meetingRepository: MeetingRepository,
    private val getMyIdUseCase: GetMyIdUseCase
) {
    suspend operator fun invoke(): List<Meeting> {
        val myId = getMyIdUseCase()
        return meetingRepository.getTodayMeetings(1, myId)//todo workspaceId
    }
}