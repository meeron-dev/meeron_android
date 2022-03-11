package forutune.meeron.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkspaceUsers(
    @SerialName("workspaceUsers") val workspaceUsers: List<WorkspaceUser>
)

@Serializable
data class WorkspaceUser(
    @SerialName("workspaceUserId") val workspaceUserId: Long = -1,
    @SerialName("workspaceId") val workspaceId: Long = -1,
    @SerialName("nickname") val nickname: String = "",
    @SerialName("profileImageUrl") val profileImageUrl: String = "",
    @SerialName("position") val position: String = "",
    @SerialName("workspaceAdmin") val workspaceAdmin: Boolean = false
)