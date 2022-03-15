package forutune.meeron.domain.model

import java.io.Serializable

@kotlinx.serialization.Serializable
data class Meeting(
    val meetingId: Long = -1,
    val title: String = "",
    val date: Date = Date(),
    val time: String = "",
    val purpose: String = "",
    val ownerIds: List<Long> = emptyList(),
    val team: Team = Team(),
    val agenda: List<Agenda> = emptyList(),
    val participants: List<WorkspaceUser> = emptyList(),
    val status: MeetingStatus = MeetingStatus.CREATING
) : Serializable

enum class MeetingStatus {
    CREATING, EXPECT, END;

    companion object {
        fun getStatus(value: String): MeetingStatus {
            return valueOf(value)
        }
    }
}