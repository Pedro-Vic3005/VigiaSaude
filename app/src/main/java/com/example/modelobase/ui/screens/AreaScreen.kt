package com.example.modelobase.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modelobase.domain.Occurrence
import com.example.modelobase.domain.ReportCategory
import com.example.modelobase.ui.components.InformationCard
import com.example.modelobase.ui.components.PageTitle
import com.example.modelobase.ui.theme.Border
import com.example.modelobase.ui.theme.Cream
import com.example.modelobase.ui.theme.Mint
import com.example.modelobase.ui.theme.Muted
import com.example.modelobase.ui.theme.Navy
import com.example.modelobase.ui.theme.Orange
import com.example.modelobase.ui.theme.Teal

@Composable
fun AreaScreen(records: List<Occurrence>) {
    var category by remember { mutableStateOf("todas") }
    val publiclyCounted = records.filter { !it.isPending && !it.isDraft }
    val filtered = publiclyCounted.filter { category == "todas" || it.category.code == category }
    val groups = filtered.groupBy { it.neighborhood.trim().ifBlank { "Não informado" } }.toList().sortedByDescending { it.second.size }
    Column(Modifier.verticalScroll(rememberScrollState()).padding(19.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        PageTitle("Explorar", "Olhe seu território", "Uma visão por bairros, sem revelar pontos ou fotografias de residências.")
        Surface(color = Mint, shape = RoundedCornerShape(24.dp), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Ilustração conceitual • não é um mapa geográfico", color = Navy, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Canvas(Modifier.fillMaxWidth().height(150.dp)) {
                    val p = Path().apply {
                        moveTo(0f, size.height * .68f)
                        cubicTo(size.width*.28f, size.height*.14f, size.width*.47f, size.height*1.18f, size.width, size.height*.35f)
                    }
                    drawPath(p, Teal.copy(alpha = .42f), style = Stroke(width = 29.dp.toPx(), cap = StrokeCap.Round))
                    drawPath(p, Color.White.copy(alpha = .65f), style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round))
                    listOf(Offset(size.width*.22f,size.height*.46f), Offset(size.width*.49f,size.height*.66f), Offset(size.width*.8f,size.height*.36f)).forEachIndexed { i, c ->
                        drawCircle(if (i==1) Orange else Teal, radius = 15.dp.toPx(), center = c)
                        drawCircle(Color.White, radius = 6.dp.toPx(), center = c)
                    }
                }
                Text("Dados de demonstração. Quantidade de relatos não equivale à intensidade de risco.", color = Muted, fontSize = 12.sp)
            }
        }
        InformationCard("Privacidade por padrão", "Nesta versão, exibimos apenas contagens por bairro. Localizações exatas e fotos privadas não são publicadas.")
        Text("Filtrar por situação", style = MaterialTheme.typography.titleMedium, color = Navy)
        FilterChip(selected = category == "todas", onClick = { category = "todas" }, label = { Text("Todas as situações") })
        ReportCategory.entries.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { value ->
                    FilterChip(selected = category == value.code, onClick = { category = value.code }, label = { Text(value.label, fontSize = 11.sp) }, modifier = Modifier.weight(1f))
                }
            }
        }
        Text("Registros por bairro", style = MaterialTheme.typography.titleLarge, color = Navy)
        if (groups.isEmpty()) Text("Nenhuma informação disponível para este filtro.", color = Muted)
        groups.forEach { (bairro, list) ->
            Surface(color = Color.White, shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(Icons.Outlined.Place, null, tint = Teal)
                    Column(Modifier.weight(1f)) {
                        Text(bairro, color = Navy, fontWeight = FontWeight.Bold)
                        Text("${list.size} registro(s) demonstrativos", color = Muted, fontSize = 12.sp)
                    }
                    Text("${list.size}", color = Teal, style = MaterialTheme.typography.titleLarge)
                }
            }
        }
        Text("A ausência de relatos não indica ausência de risco. Futuramente, o Google Maps poderá apresentar áreas agregadas com dados validados.", color = Muted, fontSize = 12.sp)
    }
}
