package com.example.modelobase.domain

import java.util.UUID

/** Códigos iguais aos do catálogo inicial do esquema PostgreSQL/PostGIS aprovado. */
enum class ReportCategory(val code: String, val label: String, val example: String, val pictogram: String) {
    AEDES("aedes_criadouro", "Mosquitos e criadouros", "Água parada, possíveis focos", "🦟"),
    RODENTS("roedores", "Roedores", "Ratos ou sinais de infestação", "🐀"),
    SCORPIONS("escorpioes", "Escorpiões", "Presença em quintais ou calçadas", "🦂"),
    SNAKES("serpentes", "Serpentes", "Aparição em local de circulação", "🐍"),
    SANITARY("infestacao_sanitaria", "Pragas sanitárias", "Infestação de insetos em ambiente urbano", "🪳"),
    UNKNOWN("nao_identificado", "Não sei identificar", "Outra situação que merece atenção", "🔎");

    companion object {
        fun fromCode(code: String): ReportCategory = entries.find { it.code == code } ?: UNKNOWN
    }
}

enum class OccurrenceStatus(val wire: String, val visible: String) {
    RECEIVED("recebida", "Registro recebido"),
    TRIAGE("em_triagem", "Em análise"),
    WAITING_INSPECTION("aguardando_vistoria", "Aguardando vistoria"),
    ATTENDING("em_atendimento", "Em atendimento"),
    FINISHED("concluida", "Atendimento concluído"),
    DISCARDED("descartada", "Registro não confirmado");

    companion object {
        fun fromWire(code: String) = entries.firstOrNull { it.wire == code } ?: RECEIVED
    }
}

enum class CommunityPerception(val wire: String, val visible: String) {
    IMPROVED("melhorou", "Melhorou"),
    CONTINUES("persiste", "Ainda acontece"),
    UNKNOWN("nao_sabe", "Não sei informar")
}

data class StatusEvent(
    val status: OccurrenceStatus,
    val timestampMillis: Long,
    val note: String = ""
)

data class Occurrence(
    val id: String = UUID.randomUUID().toString(),
    val idempotencyUuid: String = UUID.randomUUID().toString(),
    val category: ReportCategory,
    val description: String,
    val neighborhood: String,
    val locationNote: String,
    val photoPath: String? = null,
    val createdMillis: Long = System.currentTimeMillis(),
    val status: OccurrenceStatus = OccurrenceStatus.RECEIVED,
    val isPending: Boolean = true,
    val isDraft: Boolean = false,
    val demoProtocol: String? = null,
    val isExample: Boolean = false,
    val timeline: List<StatusEvent> = emptyList(),
    val professionalAssessment: String? = null,
    val interventionNote: String? = null,
    val communityPerception: CommunityPerception? = null
)

/** Transições demonstrativas. Na API futura a validação definitiva será do servidor. */
object StatusWorkflow {
    fun canMove(from: OccurrenceStatus, to: OccurrenceStatus): Boolean = when (from) {
        OccurrenceStatus.RECEIVED -> to == OccurrenceStatus.TRIAGE
        OccurrenceStatus.TRIAGE -> to == OccurrenceStatus.WAITING_INSPECTION || to == OccurrenceStatus.DISCARDED
        OccurrenceStatus.WAITING_INSPECTION -> to == OccurrenceStatus.ATTENDING || to == OccurrenceStatus.DISCARDED
        OccurrenceStatus.ATTENDING -> to == OccurrenceStatus.FINISHED
        OccurrenceStatus.FINISHED, OccurrenceStatus.DISCARDED -> false
    }
}
