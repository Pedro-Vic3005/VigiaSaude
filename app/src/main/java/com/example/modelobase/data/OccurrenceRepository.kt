package com.example.modelobase.data

import com.example.modelobase.domain.CommunityPerception
import com.example.modelobase.domain.Occurrence
import com.example.modelobase.domain.OccurrenceStatus

/** Contrato agnóstico à origem de dados para a futura implementação com API real. */
interface OccurrenceRepository {
    val records: List<Occurrence>
    fun store(occurrence: Occurrence)
    fun removeDraft(id: String)
    fun simulateReceipt(ids: List<String>)
    fun advanceStatus(id: String, target: OccurrenceStatus, note: String = "")
    fun addPerception(id: String, perception: CommunityPerception)
    fun resetDemo()
}

/** Contratos planejados; nesta versão não há chamadas à rede. */
interface FutureApiGateway {
    suspend fun submitWithIdempotency(occurrence: Occurrence): String
    suspend fun fetchStatus(id: String, accessSecret: String): OccurrenceStatus
}

interface FutureGeoProvider {
    suspend fun selectCoordinate(): Pair<Double, Double>?
}
