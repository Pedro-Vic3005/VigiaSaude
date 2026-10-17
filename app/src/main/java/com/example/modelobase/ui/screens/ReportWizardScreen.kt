package com.example.modelobase.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modelobase.data.PhotoStore
import com.example.modelobase.domain.Occurrence
import com.example.modelobase.domain.ReportCategory
import com.example.modelobase.ui.components.CategoryTile
import com.example.modelobase.ui.components.InformationCard
import com.example.modelobase.ui.components.OutlineAction
import com.example.modelobase.ui.components.PrimaryAction
import com.example.modelobase.ui.theme.Border
import com.example.modelobase.ui.theme.Cream
import com.example.modelobase.ui.theme.Muted
import com.example.modelobase.ui.theme.Navy
import com.example.modelobase.ui.theme.Teal
import com.example.modelobase.ui.theme.Warning
import java.util.UUID

private val suggestedNeighborhoods = listOf("Boa Vista", "Casa Amarela", "Várzea", "Afogados", "Ibura", "Boa Viagem", "Dois Unidos", "Santo Amaro")

@Composable
fun ReportWizardScreen(
    initialCategory: ReportCategory?,
    initialDraft: Occurrence?,
    simulatedOffline: Boolean,
    onStore: (Occurrence, Boolean) -> Unit,
    onCancel: () -> Unit,
    onCompleted: (String) -> Unit
) {
    val context = LocalContext.current
    var step by rememberSaveable(initialDraft?.id) { mutableIntStateOf(0) }
    var categoryCode by rememberSaveable(initialDraft?.id) { mutableStateOf((initialDraft?.category ?: initialCategory ?: ReportCategory.AEDES).code) }
    var description by rememberSaveable(initialDraft?.id) { mutableStateOf(initialDraft?.description.orEmpty()) }
    var neighborhood by rememberSaveable(initialDraft?.id) { mutableStateOf(initialDraft?.neighborhood.orEmpty()) }
    var reference by rememberSaveable(initialDraft?.id) { mutableStateOf(initialDraft?.locationNote.orEmpty()) }
    var photoPath by rememberSaveable(initialDraft?.id) { mutableStateOf(initialDraft?.photoPath) }
    var photoError by remember { mutableStateOf<String?>(null) }
    var showErrors by remember { mutableStateOf(false) }
    val localId = remember(initialDraft?.id) { initialDraft?.id ?: UUID.randomUUID().toString() }
    val idemId = remember(initialDraft?.id) { initialDraft?.idempotencyUuid ?: UUID.randomUUID().toString() }
    val pickedPhoto = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            runCatching { PhotoStore.importPhoto(context, uri) }
                .onSuccess { photoPath = it; photoError = null }
                .onFailure { photoError = it.message ?: "Não foi possível importar a imagem." }
        }
    }

    fun createRecord(isDraft: Boolean): Occurrence = Occurrence(
        id = localId, idempotencyUuid = idemId,
        category = ReportCategory.fromCode(categoryCode),
        description = description.trim(), neighborhood = neighborhood.trim(),
        locationNote = reference.trim(), photoPath = photoPath,
        createdMillis = initialDraft?.createdMillis ?: System.currentTimeMillis(),
        isDraft = isDraft, isPending = true
    )

    fun leave() {
        if (description.isNotBlank() || neighborhood.isNotBlank() || photoPath != null) onStore(createRecord(true), false)
        onCancel()
    }

    BackHandler {
        if (step > 0) step-- else leave()
    }

    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(19.dp), verticalArrangement = Arrangement.spacedBy(19.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { if (step > 0) step-- else leave() }, modifier = Modifier.size(48.dp)) {
                Icon(Icons.Outlined.ArrowBack, contentDescription = "Voltar", tint = Navy)
            }
            Column(Modifier.weight(1f)) {
                Text("REGISTRAR OCORRÊNCIA", fontSize = 12.sp, color = Teal, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Text("Passo ${step + 1} de 3", style = MaterialTheme.typography.titleLarge, color = Navy)
            }
            Text("${step + 1}/3", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Muted)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            repeat(3) { i -> Box(Modifier.weight(1f).height(7.dp).background(if (i <= step) Teal else Border, RoundedCornerShape(7.dp))) }
        }

        when (step) {
            0 -> {
                Text("O que você observou?", style = MaterialTheme.typography.headlineMedium, color = Navy)
                Text("Selecione uma situação e conte o que aconteceu. Não precisa saber o nome exato do animal.", color = Muted)
                ReportCategory.entries.chunked(2).forEach { pair ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                        pair.forEach { category ->
                            CategoryTile(category, category.code == categoryCode, onClick = { categoryCode = category.code }, modifier = Modifier.weight(1f))
                        }
                        if (pair.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
                OutlinedTextField(
                    value = description, onValueChange = { if (it.length <= 2000) description = it },
                    label = { Text("Descreva o que você encontrou") },
                    placeholder = { Text("Ex.: encontrei água acumulada próxima à calçada") },
                    supportingText = { Text("${description.length}/2000 • mínimo 10 caracteres") },
                    isError = showErrors && description.trim().length < 10,
                    minLines = 3, maxLines = 6, shape = RoundedCornerShape(17.dp), modifier = Modifier.fillMaxWidth()
                )
                if (showErrors && description.trim().length < 10) Text("Escreva pelo menos 10 caracteres para continuar.", color = MaterialTheme.colorScheme.error)
                PrimaryAction("Continuar: local da ocorrência", onClick = {
                    showErrors = true
                    if (description.trim().length >= 10) { step = 1; showErrors = false }
                })
            }
            1 -> {
                Text("Onde você encontrou isso?", style = MaterialTheme.typography.headlineMedium, color = Navy)
                InformationCard("Localização no MVP", "Escolha o bairro e informe um ponto de referência. A marcação exata pelo mapa será integrada futuramente.", Icons.Outlined.LocationOn)
                OutlinedTextField(
                    value = neighborhood, onValueChange = { neighborhood = it.take(90) },
                    label = { Text("Bairro do Recife") },
                    placeholder = { Text("Ex.: Várzea") },
                    singleLine = true,
                    isError = showErrors && neighborhood.isBlank(),
                    shape = RoundedCornerShape(17.dp), modifier = Modifier.fillMaxWidth()
                )
                Text("Bairros sugeridos", fontWeight = FontWeight.Bold, color = Navy)
                suggestedNeighborhoods.chunked(2).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                        row.forEach { b ->
                            FilterChip(selected = neighborhood == b, onClick = { neighborhood = b }, label = { Text(b, fontSize = 11.sp) }, modifier = Modifier.weight(1f))
                        }
                    }
                }
                OutlinedTextField(
                    value = reference, onValueChange = { reference = it.take(160) },
                    label = { Text("Ponto de referência (opcional)") },
                    placeholder = { Text("Ex.: perto de uma praça") },
                    supportingText = { Text("Não informe número da casa ou dados pessoais.") },
                    minLines = 2, maxLines = 3, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(17.dp)
                )
                if (showErrors && neighborhood.isBlank()) Text("Informe o bairro para continuar.", color = MaterialTheme.colorScheme.error)
                PrimaryAction("Continuar: revisar", onClick = {
                    showErrors = true
                    if (neighborhood.isNotBlank()) { step = 2; showErrors = false }
                })
            }
            else -> {
                Text("Tudo certo para registrar?", style = MaterialTheme.typography.headlineMedium, color = Navy)
                Text("Confira os dados antes de salvar.", color = Muted)
                Surface(shape = RoundedCornerShape(21.dp), color = androidx.compose.ui.graphics.Color.White, modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(11.dp)) {
                        Text("${ReportCategory.fromCode(categoryCode).pictogram}  ${ReportCategory.fromCode(categoryCode).label}", color = Navy, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        HorizontalDivider(color = Border)
                        Text(description, color = Navy)
                        Text("Bairro: $neighborhood", color = Muted)
                        if (reference.isNotBlank()) Text("Referência: $reference", color = Muted)
                    }
                }
                Surface(
                    onClick = { pickedPhoto.launch("image/*") },
                    color = Cream, shape = RoundedCornerShape(19.dp), modifier = Modifier.fillMaxWidth()
                ) {
                    Row(Modifier.padding(17.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        Icon(Icons.Outlined.CameraAlt, null, tint = Teal)
                        Column(Modifier.weight(1f)) {
                            Text(if (photoPath == null) "Adicionar foto (opcional)" else "Foto guardada no aparelho", fontWeight = FontWeight.Bold, color = Navy)
                            Text("JPG, PNG ou WebP • até 10 MB", fontSize = 12.sp, color = Muted)
                        }
                        if (photoPath != null) IconButton(onClick = { photoPath = null }) { Icon(Icons.Outlined.DeleteOutline, "Remover foto", tint = Warning) }
                    }
                }
                val preview = remember(photoPath) { PhotoStore.thumbnail(photoPath) }
                if (preview != null) {
                    Image(bitmap = preview.asImageBitmap(), contentDescription = "Fotografia selecionada, apenas neste dispositivo", contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().height(176.dp).background(Cream, RoundedCornerShape(16.dp)))
                }
                if (photoError != null) Text(photoError.orEmpty(), color = MaterialTheme.colorScheme.error)
                InformationCard("Não se coloque em risco", "A fotografia é opcional. Nunca se aproxime, capture ou manipule animais perigosos para registrar.", Icons.Outlined.WarningAmber)
                InformationCard("Privacidade", "As informações ficarão somente neste aparelho. Nenhum dado será transmitido nesta versão demonstrativa.", Icons.Outlined.Info)
                PrimaryAction(if (simulatedOffline) "Salvar para sincronizar depois" else "Simular recebimento do registro", onClick = {
                    val newReport = createRecord(false)
                    onStore(newReport, !simulatedOffline)
                    onCompleted(localId)
                })
                OutlineAction("Guardar como rascunho", onClick = { onStore(createRecord(true), false); onCancel() })
            }
        }
    }
}
