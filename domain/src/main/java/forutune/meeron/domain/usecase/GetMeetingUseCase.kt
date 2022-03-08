package forutune.meeron.domain.usecase

import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.repository.MeetingRepository
import javax.inject.Inject

class GetMeetingUseCase @Inject constructor(
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(meetingId: Long): Meeting {
        return meetingRepository.getMeeting(meetingId)
    }
}