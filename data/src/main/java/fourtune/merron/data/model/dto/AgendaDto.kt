package fourtune.merron.data.model.dto

import forutune.meeron.domain.model.Agenda
import kotlinx.serialization.Serializable

@Serializable
data class AgendaDto(
    val agendas :List<Agenda>
)
