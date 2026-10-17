package com.example.modelobase.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.modelobase.domain.Occurrence
import com.example.modelobase.ui.components.EmptyState
import com.example.modelobase.ui.components.OccurrenceCard
import com.example.modelobase.ui.components.PageTitle
import com.example.modelobase.ui.components.PrimaryAction
import com.example.modelobase.ui.theme.Mint
import com.example.modelobase.ui.theme.Muted
import com.example.modelobase.ui.theme.Navy

@Composable
fun MyReportsScreen(records: List<Occurrence>, onDetail: (String) -> Unit, onNew: () -> Unit) {
    val personal = records.filterNot { it.isExample }.sortedByDescending { it.createdMillis }
    Column(Modifier.verticalScroll(rememberScrollState()).padding(19.dp), verticalArrangement = Arrangement.spacedBy(19.dp)) {
        PageTitle("Acompanhar", "Meus registros", "Veja o andamento dos relatos deste aparelho. Os exemplos fictícios ficam no painel de demonstração.")
        Surface(color = Mint, shape = RoundedCornerShape(21.dp), modifier = Modifier.fillMaxWidth()) {
            Row(Modifier.padding(17.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                Column { Text("${personal.size}", style = MaterialTheme.typography.headlineMedium, color = Navy); Text("Registros", color = Muted) }
                Column { Text("${personal.count { it.isPending && !it.isDraft }}", style = MaterialTheme.typography.headlineMedium, color = Navy); Text("Pendentes", color = Muted) }
                Column { Text("${personal.count { it.isDraft }}", style = MaterialTheme.typography.headlineMedium, color = Navy); Text("Rascunhos", color = Muted) }
            }
        }
        if (personal.isEmpty()) {
            EmptyState("Nenhum registro por aqui", "Você pode registrar uma situação do seu bairro para experimentar o fluxo.")
            PrimaryAction("Fazer meu primeiro registro", onClick = onNew)
        } else {
            personal.forEach { OccurrenceCard(it, onClick = { onDetail(it.id) }) }
            PrimaryAction("Novo registro", onClick = onNew)
        }
    }
}
