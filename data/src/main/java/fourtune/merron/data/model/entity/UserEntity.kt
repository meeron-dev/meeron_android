package fourtune.merron.data.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = TeamEntity::class,
            parentColumns = ["id"],
            childColumns = ["teamId"],
            onDelete = CASCADE
        )
    ]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val teamId: Long,
    val name: String,
    val email: String,
    val profile: String
)
