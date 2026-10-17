package com.example.modelobase.data

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import com.example.modelobase.domain.CommunityPerception
import com.example.modelobase.domain.Occurrence
import com.example.modelobase.domain.OccurrenceStatus
import com.example.modelobase.domain.ReportCategory
import com.example.modelobase.domain.StatusEvent
import com.example.modelobase.domain.StatusWorkflow
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

/** MVP: todos os relatos são locais. Não implementa rede, Prefeitura ou autenticação de agentes. */
class LocalOccurrenceRepository(private val context: Context) : OccurrenceRepository {
    private val storage = context.getSharedPreferences("vigiasaude_mvp_local_v1", Context.MODE_PRIVATE)
    private val current = mutableStateListOf<Occurrence>()
    override val records: List<Occurrence> get() = current

    var simulateOffline: Boolean
        get() = storage.getBoolean("simulateOffline", false)
        set(value) { storage.edit().putBoolean("simulateOffline", value).apply() }

    init {
        val contents = storage.getString("records", null)
        if (contents == null) {
            current.addAll(seed())
            persist()
        } else runCatching {
            val json = JSONArray(contents)
            for (index in 0 until json.length()) current.add(fromJson(json.getJSONObject(index)))
        }.onFailure {
            // Falha de leitura não elimina o original; permite um novo registro nesta sessão.
            current.clear()
        }
    }

    override fun store(occurrence: Occurrence) {
        val position = current.indexOfFirst { it.id == occurrence.id }
        if (position >= 0) current[position] = occurrence else current.add(0, occurrence)
        persist()
    }

    override fun removeDraft(id: String) {
        val position = current.indexOfFirst { it.id == id && it.isDraft }
        if (position >= 0) { current.removeAt(position); persist() }
    }

    /** Simula aceite e gera número marcado DEMO; não é protocolo de órgão público. */
    override fun simulateReceipt(ids: List<String>) {
        ids.forEach { id ->
            val i = current.indexOfFirst { it.id == id && it.isPending && !it.isDraft }
            if (i >= 0) {
                val before = current[i]
                val short = before.id.replace("-", "").take(7).uppercase()
                current[i] = before.copy(
                    isPending = false,
                    demoProtocol = "DEMO-$short",
                    timeline = before.timeline + StatusEvent(OccurrenceStatus.RECEIVED, System.currentTimeMillis(), "Recebimento simulado no aparelho")
                )
            }
        }
        persist()
    }

    override fun advanceStatus(id: String, target: OccurrenceStatus, note: String) {
        val i = current.indexOfFirst { it.id == id }
        if (i < 0) return
        val old = current[i]
        if (old.isDraft || old.isPending || !StatusWorkflow.canMove(old.status, target)) return
        val next = old.copy(
            status = target,
            professionalAssessment = if (target == OccurrenceStatus.DISCARDED || target == OccurrenceStatus.ATTENDING) note else old.professionalAssessment,
            interventionNote = if (target == OccurrenceStatus.FINISHED) note else old.interventionNote,
            timeline = old.timeline + StatusEvent(target, System.currentTimeMillis(), note)
        )
        current[i] = next
        persist()
    }

    override fun addPerception(id: String, perception: CommunityPerception) {
        val i = current.indexOfFirst { it.id == id && it.status == OccurrenceStatus.FINISHED }
        if (i >= 0) {
            current[i] = current[i].copy(communityPerception = perception)
            persist()
        }
    }

    override fun resetDemo() {
        java.io.File(context.filesDir, "evidencias").listFiles()?.forEach { it.delete() }
        current.clear()
        current.addAll(seed())
        simulateOffline = false
        persist()
    }

    private fun persist() {
        val json = JSONArray()
        current.forEach { json.put(toJson(it)) }
        storage.edit().putString("records", json.toString()).apply()
    }

    private fun toJson(r: Occurrence): JSONObject = JSONObject().apply {
        put("id", r.id); put("idempotencyUuid", r.idempotencyUuid)
        put("category", r.category.code); put("description", r.description)
        put("neighborhood", r.neighborhood); put("locationNote", r.locationNote)
        put("photoPath", r.photoPath ?: JSONObject.NULL); put("createdMillis", r.createdMillis)
        put("status", r.status.wire); put("isPending", r.isPending); put("isDraft", r.isDraft)
        put("demoProtocol", r.demoProtocol ?: JSONObject.NULL); put("isExample", r.isExample)
        put("professionalAssessment", r.professionalAssessment ?: JSONObject.NULL)
        put("interventionNote", r.interventionNote ?: JSONObject.NULL)
        put("communityPerception", r.communityPerception?.wire ?: JSONObject.NULL)
        put("timeline", JSONArray().apply {
            r.timeline.forEach { event ->
                put(JSONObject().apply {
                    put("status", event.status.wire)
                    put("timestampMillis", event.timestampMillis)
                    put("note", event.note)
                })
            }
        })
    }

    private fun fromJson(j: JSONObject): Occurrence {
        val list = mutableListOf<StatusEvent>()
        val events = j.optJSONArray("timeline") ?: JSONArray()
        for (i in 0 until events.length()) {
            val event = events.getJSONObject(i)
            list.add(StatusEvent(OccurrenceStatus.fromWire(event.optString("status")), event.optLong("timestampMillis"), event.optString("note")))
        }
        return Occurrence(
            id = j.optString("id", UUID.randomUUID().toString()),
            idempotencyUuid = j.optString("idempotencyUuid", UUID.randomUUID().toString()),
            category = ReportCategory.fromCode(j.optString("category")),
            description = j.optString("description"),
            neighborhood = j.optString("neighborhood"),
            locationNote = j.optString("locationNote"),
            photoPath = j.optString("photoPath").takeIf { it.isNotBlank() && it != "null" },
            createdMillis = j.optLong("createdMillis", System.currentTimeMillis()),
            status = OccurrenceStatus.fromWire(j.optString("status", "recebida")),
            isPending = j.optBoolean("isPending", true),
            isDraft = j.optBoolean("isDraft", false),
            demoProtocol = j.optString("demoProtocol").takeIf { it.isNotBlank() && it != "null" },
            isExample = j.optBoolean("isExample", false),
            professionalAssessment = j.optString("professionalAssessment").takeIf { it.isNotBlank() && it != "null" },
            interventionNote = j.optString("interventionNote").takeIf { it.isNotBlank() && it != "null" },
            communityPerception = CommunityPerception.entries.firstOrNull { it.wire == j.optString("communityPerception") },
            timeline = list
        )
    }

    private fun seed(): List<Occurrence> {
        val now = System.currentTimeMillis()
        fun fake(category: ReportCategory, bairro: String, description: String, state: OccurrenceStatus, offset: Long): Occurrence {
            val id = UUID.randomUUID().toString()
            return Occurrence(
                id = id, category = category, description = description, neighborhood = bairro,
                locationNote = "Região aproximada — exemplo ilustrativo",
                createdMillis = now - offset, status = state,
                isPending = false, demoProtocol = "DEMO-${id.replace("-", "").take(7).uppercase()}",
                isExample = true,
                timeline = listOf(StatusEvent(OccurrenceStatus.RECEIVED, now - offset, "Exemplo fictício")) + (if (state == OccurrenceStatus.RECEIVED) emptyList() else listOf(StatusEvent(state, now - offset / 2, "Avanço fictício de demonstração")))
            )
        }
        return listOf(
            fake(ReportCategory.AEDES, "Casa Amarela", "Relato fictício de recipientes com água acumulada em terreno.", OccurrenceStatus.TRIAGE, 72_000_000),
            fake(ReportCategory.SCORPIONS, "Várzea", "Relato fictício de avistamento de escorpiões em área comum.", OccurrenceStatus.WAITING_INSPECTION, 135_000_000),
            fake(ReportCategory.RODENTS, "Boa Vista", "Relato fictício de presença recorrente de roedores em via pública.", OccurrenceStatus.RECEIVED, 180_000_000)
        )
    }
}
