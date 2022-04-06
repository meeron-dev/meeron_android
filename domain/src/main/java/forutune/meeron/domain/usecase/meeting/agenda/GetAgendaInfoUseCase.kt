package forutune.meeron.domain.usecase.meeting.agenda

import forutune.meeron.domain.model.AgendaInfo
import forutune.meeron.domain.repository.MeetingRepository
import javax.inject.Inject

class GetAgendaInfoUseCase @Inject constructor(
    private val meetingRepository: MeetingRepository
) {
    suspend operator fun invoke(meetingId: Long) :AgendaInfo{
        return meetingRepository.getAgendaInfo(meetingId)
    }
}