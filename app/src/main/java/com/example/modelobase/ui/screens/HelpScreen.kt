package com.example.modelobase.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import com.example.modelobase.ui.components.InformationCard
import com.example.modelobase.ui.components.OutlineAction
import com.example.modelobase.ui.components.PageTitle
import com.example.modelobase.ui.components.PrimaryAction
import com.example.modelobase.ui.theme.Muted
import com.example.modelobase.ui.theme.Navy
import com.example.modelobase.ui.theme.Teal

@Composable
fun HelpScreen(
    isSimulatingOffline: Boolean,
    pendingCount: Int,
    onOfflineToggle: (Boolean) -> Unit,
    onSimulateSync: () -> Unit,
    onOpenAgent: () -> Unit,
    onReset: () -> Unit
) {
    var showConfirm by remember { mutableStateOf(false) }
    Column(Modifier.verticalScroll(rememberScrollState()).padding(19.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        PageTitle("Ajuda", "Como funciona?", "Respostas simples e ferramentas para testar o protótipo.")
        InformationCard("1 • Registre", "Escolha uma situação, descreva o que viu e informe o bairro. Foto é opcional.")
        InformationCard("2 • Acompanhe", "O registro fica armazenado no aparelho. A opção de recebimento é uma simulação local, sem servidor.")
        InformationCard("3 • Valide", "No modo agente, você pode simular a triagem, vistoria e conclusão. Nada representa uma ação real.")
        InformationCard("Em caso de perigo", "Não toque em animais suspeitos nem tente coletá-los. Se houver acidentes, procure atendimento de saúde ou serviço de urgência; o app não substitui esses canais.", Icons.Outlined.HealthAndSafety)

        Surface(color = Color.White, shape = RoundedCornerShape(23.dp), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(17.dp), verticalArrangement = Arrangement.spacedBy(13.dp)) {
                Text("Laboratório de demonstração", style = MaterialTheme.typography.titleLarge, color = Navy)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Simular sem conexão", fontWeight = FontWeight.Bold, color = Navy)
                        Text("Força o próximo registro a ficar pendente localmente.", color = Muted, style = MaterialTheme.typography.bodyMedium)
                    }
                    Switch(checked = isSimulatingOffline, onCheckedChange = onOfflineToggle)
                }
                InformationCard("Pendentes no aparelho: $pendingCount", "Use o botão abaixo para simular o recebimento local. Não há envio externo.", Icons.Outlined.CloudOff)
                PrimaryAction("Simular sincronização", onClick = onSimulateSync, enabled = pendingCount > 0 && !isSimulatingOffline)
                OutlineAction("Abrir painel demonstrativo do agente", onClick = onOpenAgent)
            }
        }
        Surface(color = Color(0xFFFFF4E7), shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(17.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
                Text("Restaurar exemplos", fontWeight = FontWeight.Bold, color = Navy)
                Text("Apaga os registros de demonstração salvos no aplicativo e restaura os três exemplos iniciais.", color = Muted)
                TextButton(onClick = { showConfirm = true }) { Text("Restaurar dados do MVP", color = Teal) }
            }
        }
        Text("Esta interface é acadêmica. UNICAP, EMPREL e Prefeitura são atores propostos, não integrações ou endossos já realizados.", color = Muted, style = MaterialTheme.typography.bodyMedium)
    }
    if (showConfirm) AlertDialog(
        onDismissRequest = { showConfirm = false },
        title = { Text("Apagar dados locais?") },
        text = { Text("Essa ação restaura a demonstração inicial e não pode ser desfeita.") },
        confirmButton = { TextButton(onClick = { onReset(); showConfirm = false }) { Text("Restaurar") } },
        dismissButton = { TextButton(onClick = { showConfirm = false }) { Text("Cancelar") } }
    )
}
