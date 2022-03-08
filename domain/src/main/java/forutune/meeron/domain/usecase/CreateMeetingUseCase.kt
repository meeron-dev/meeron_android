package forutune.meeron.domain.usecase

import forutune.meeron.domain.model.Meeting
import forutune.meeron.domain.repository.MeetingRepository
import javax.inject.Inject

class CreateMeetingUseCase @Inject constructor(
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(meeting: Meeting): Long {
        return meetingRepository.createMeeting(meeting)
    }
}