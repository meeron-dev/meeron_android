package forutune.meeron.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkSpace(
    val workspaceId: Long = -1,
    val workspaceName: String = "",
    val nickname: String = "",
    val position: String = "",
    val email: String = "",
    val phone: String = ""
) : java.io.Serializable
