package fourtune.merron.data.model.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkSpaceUserIds(
    @SerialName("workspaceUserIds") val workspaceUserIds: List<Long>
)
