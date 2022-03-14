package fourtune.merron.data.model.dto.request

import forutune.meeron.domain.model.Agenda
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkSpaceUserIdsRequest(
    @SerialName("workspaceUserIds") val workspaceUserIds: List<Long>
)

@Serializable
data class AgendaRequest(
    val agendas :List<Agenda>
)