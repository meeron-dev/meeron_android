package fourtune.merron.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import forutune.meeron.domain.model.Meeting

@Entity
data class MeetingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String = "",
    val date: String = "",
    val time: String = "",
    val purpose: String = "",
) {
    companion object {
        fun from(meeting: Meeting) =
            with(meeting) {
                MeetingEntity(
                    title = meetingName,
                    date = date.displayString(),
                    time = time,
                    purpose = purpose,
                )
            }
    }
}

