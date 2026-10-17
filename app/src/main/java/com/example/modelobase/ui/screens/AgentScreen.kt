package com.example.modelobase.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modelobase.domain.Occurrence
import com.example.modelobase.domain.OccurrenceStatus
import com.example.modelobase.ui.components.InformationCard
import com.example.modelobase.ui.components.OccurrenceCard
import com.example.modelobase.ui.components.PageTitle
import com.example.modelobase.ui.components.PrimaryAction
import com.example.modelobase.ui.components.StatusPill
import com.example.modelobase.ui.theme.Mint
import com.example.modelobase.ui.theme.Muted
import com.example.modelobase.ui.theme.Navy
import com.example.modelobase.ui.theme.Teal
import com.example.modelobase.ui.theme.Wine

@Composable
fun AgentScreen(records: List<Occurrence>, onBack: () -> Unit, onAdvance: (String, OccurrenceStatus, String) -> Unit) {
    var selectedId by remember { mutableStateOf<String?>(null) }
    var note by remember(selectedId) { mutableStateOf("") }
    var confirm by remember { mutableStateOf<OccurrenceStatus?>(null) }
    var onlyPending by remember { mutableStateOf(false) }
    val selection = records.find { it.id == selectedId }
    BackHandler { if (selectedId != null) selectedId = null else onBack() }

    Column(Modifier.verticalScroll(rememberScrollState()).padding(19.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { if (selectedId != null) selectedId = null else onBack() }) {
                Icon(Icons.Outlined.ArrowBack, "Voltar", tint = Navy)
            }
            Text("Área de agentes • demonstração", color = Navy, fontWeight = FontWeight.Bold)
        }
        InformationCard("Acesso simulado", "Esta tela NÃO possui autenticação real. Os registros, status e intervenções são locais e fictícios. Não usar com dados pessoais ou como painel público.", Icons.Outlined.WarningAmber)
        if (selection == null) {
            PageTitle("Visão operacional", "Painel da Vigilância", "Exercício de triagem e validação humana para demonstrar o ciclo do projeto.")
            Surface(color = Mint, shape = RoundedCornerShape(22.dp), modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(17.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Column { Text("${records.count { !it.isPending && !it.isDraft }}", style = MaterialTheme.typography.headlineMedium, color = Navy); Text("Registros demo", fontSize = 12.sp, color = Muted) }
                    Column { Text("${records.count { it.status == OccurrenceStatus.RECEIVED && !it.isPending }}", style = MaterialTheme.typography.headlineMedium, color = Navy); Text("Recebidos", fontSize = 12.sp, color = Muted) }
                    Column { Text("${records.count { it.status == OccurrenceStatus.FINISHED }}", style = MaterialTheme.typography.headlineMedium, color = Navy); Text("Concluídos", fontSize = 12.sp, color = Muted) }
                }
            }
            FilterChip(selected = onlyPending, onClick = { onlyPending = !onlyPending }, label = { Text("Somente aguardando triagem") })
            val filtered = records.filter { !it.isPending && !it.isDraft }.filter { !onlyPending || it.status == OccurrenceStatus.RECEIVED }.sortedByDescending { it.createdMillis }
            if (filtered.isEmpty()) Text("Nenhuma ocorrência neste filtro.", color = Muted)
            filtered.forEach { OccurrenceCard(it, onClick = { selectedId = it.id }) }
        } else {
            PageTitle("Triagem técnica", selection.category.label, "${selection.neighborhood} • ${selection.demoProtocol ?: "Sem protocolo de demonstração"}")
            StatusPill(selection)
            Surface(color = Color.White, shape = RoundedCornerShape(21.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(17.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(selection.description, color = Navy, fontSize = 16.sp)
                    Text("Referência: ${selection.locationNote.ifBlank { "Não informada" }}", color = Muted)
                    Text("A foto fica privada no dispositivo, não é publicada na visão comunitária.", color = Muted, fontSize = 12.sp)
                }
            }
            OutlinedTextField(
                value = note, onValueChange = { note = it.take(600) },
                label = { Text("Observação técnica simulada") },
                placeholder = { Text("Ex.: vistoria demonstrativa registrada") },
                minLines = 3, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(17.dp)
            )
            when (selection.status) {
                OccurrenceStatus.RECEIVED -> PrimaryAction("Iniciar triagem", onClick = { confirm = OccurrenceStatus.TRIAGE })
                OccurrenceStatus.TRIAGE -> {
                    PrimaryAction("Solicitar vistoria", onClick = { confirm = OccurrenceStatus.WAITING_INSPECTION })
                    TextButton(onClick = { confirm = OccurrenceStatus.DISCARDED }) { Text("Encerrar como não confirmada", color = Wine) }
                }
                OccurrenceStatus.WAITING_INSPECTION -> {
                    PrimaryAction("Registrar validação técnica e atendimento", onClick = { confirm = OccurrenceStatus.ATTENDING })
                    TextButton(onClick = { confirm = OccurrenceStatus.DISCARDED }) { Text("Marcar como não confirmada", color = Wine) }
                }
                OccurrenceStatus.ATTENDING -> PrimaryAction("Concluir intervenção simulada", onClick = { confirm = OccurrenceStatus.FINISHED })
                OccurrenceStatus.FINISHED -> Text("Atendimento finalizado. A comunidade já pode registrar sua percepção.", color = Teal)
                OccurrenceStatus.DISCARDED -> Text("Ocorrência encerrada como não confirmada nesta simulação.", color = Wine)
            }
            Text("A validação profissional não é substituída por IA ou confirmação de moradores.", color = Muted, fontSize = 12.sp)
        }
    }
    if (confirm != null && selection != null) {
        AlertDialog(
            onDismissRequest = { confirm = null },
            title = { Text("Confirmar atualização simulada?") },
            text = { Text("Novo estado: ${confirm!!.visible}. Nenhuma instituição receberá esta informação.") },
            confirmButton = { TextButton(onClick = {
                onAdvance(selection.id, confirm!!, note.trim())
                confirm = null
                note = ""
            }) { Text("Atualizar", color = Teal) } },
            dismissButton = { TextButton(onClick = { confirm = null }) { Text("Cancelar") } }
        )
    }
}
