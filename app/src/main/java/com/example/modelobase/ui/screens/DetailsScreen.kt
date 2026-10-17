package com.example.modelobase.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modelobase.data.PhotoStore
import com.example.modelobase.domain.CommunityPerception
import com.example.modelobase.domain.Occurrence
import com.example.modelobase.domain.OccurrenceStatus
import com.example.modelobase.ui.components.InformationCard
import com.example.modelobase.ui.components.PrimaryAction
import com.example.modelobase.ui.components.StatusPill
import com.example.modelobase.ui.components.dateLabel
import com.example.modelobase.ui.theme.Border
import com.example.modelobase.ui.theme.Cream
import com.example.modelobase.ui.theme.Muted
import com.example.modelobase.ui.theme.Navy
import com.example.modelobase.ui.theme.Teal

@Composable
fun DetailsScreen(record: Occurrence?, onBack: () -> Unit, onEditDraft: (String) -> Unit, onFeedback: (String, CommunityPerception) -> Unit) {
    if (record == null) {
        Column(Modifier.padding(20.dp)) {
            Text("Registro não encontrado", color = Navy)
            PrimaryAction("Voltar", onClick = onBack)
        }
        return
    }
    Column(Modifier.verticalScroll(rememberScrollState()).padding(19.dp), verticalArrangement = Arrangement.spacedBy(17.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, "Voltar", tint = Navy) }
            Text("Detalhes do registro", style = MaterialTheme.typography.titleLarge, color = Navy)
        }
        Surface(shape = RoundedCornerShape(23.dp), color = Color.White, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(record.category.pictogram, fontSize = 36.sp)
                Text(record.category.label, style = MaterialTheme.typography.headlineMedium, color = Navy)
                StatusPill(record)
                Text(record.description, color = Navy, style = MaterialTheme.typography.bodyLarge)
                val localImage = remember(record.photoPath) { PhotoStore.thumbnail(record.photoPath) }
                if (localImage != null) Image(
                    bitmap = localImage.asImageBitmap(),
                    contentDescription = "Foto privada anexada a este registro",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(170.dp)
                )
                HorizontalDivider(color = Border)
                Text("Bairro: ${record.neighborhood}", color = Muted)
                if (record.locationNote.isNotBlank()) Text("Referência: ${record.locationNote}", color = Muted)
                Text("Criado em: ${dateLabel(record.createdMillis)}", color = Muted, fontSize = 12.sp)
                Text("Identificador local: ${record.id.take(8)}...", color = Muted, fontSize = 11.sp)
                if (record.demoProtocol != null) Text("Protocolo de simulação: ${record.demoProtocol}", color = Teal, fontWeight = FontWeight.Bold)
            }
        }
        when {
            record.isDraft -> {
                InformationCard("Rascunho guardado", "Esse registro está salvo no aparelho e ainda precisa ser concluído.")
                PrimaryAction("Continuar preenchimento", onClick = { onEditDraft(record.id) })
            }
            record.isPending -> InformationCard("Aguardando simulação de envio", "Nenhum servidor recebeu este relato. Abra Ajuda e simule a sincronização quando estiver conectado.")
            else -> InformationCard("Status de demonstração", "As atualizações neste MVP são simuladas no dispositivo. Não representam atendimento ou vistoria real.")
        }
        if (!record.isDraft && !record.isPending) {
            Text("Linha do tempo", style = MaterialTheme.typography.titleLarge, color = Navy)
            if (record.timeline.isEmpty()) Text("Ainda não há atualizações nesta simulação.", color = Muted)
            record.timeline.sortedByDescending { it.timestampMillis }.forEach { event ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(11.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Outlined.CheckCircleOutline, null, tint = Teal)
                    Column {
                        Text(event.status.visible, color = Navy, fontWeight = FontWeight.Bold)
                        Text(dateLabel(event.timestampMillis), color = Muted, fontSize = 11.sp)
                        if (event.note.isNotBlank()) Text(event.note, color = Muted, fontSize = 12.sp)
                    }
                }
            }
            if (record.status == OccurrenceStatus.FINISHED) {
                Text("Como está a situação?", style = MaterialTheme.typography.titleLarge, color = Navy)
                Text("Sua avaliação ajuda a fechar o ciclo comunitário.", color = Muted)
                CommunityPerception.entries.forEach { perception ->
                    FilterChip(
                        selected = record.communityPerception == perception,
                        onClick = { onFeedback(record.id, perception) },
                        label = { Text(perception.visible) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        Text("Atenção: este aplicativo não substitui emergência, orientação médica ou canais oficiais de vigilância.", fontSize = 12.sp, color = Muted)
    }
}
