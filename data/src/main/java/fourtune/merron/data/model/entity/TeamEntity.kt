package fourtune.merron.data.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = MeetingEntity::class,
            parentColumns = ["id"],
            childColumns = ["meetingId"],
            onDelete = CASCADE
        )
    ]
)
data class TeamEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val meetingId: Long
)